package com.ron.taskmanager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;


@Service
public class S3Service {

    private final String accessKeyId;
    private final String secretKey;
    private final String region;
    private final String bucketName;
    private final S3Client s3Client;
    public S3Service(@Value("${aws.accessKeyId}") String accessKeyId,
                     @Value("${aws.secretKey}") String secretKey,
                     @Value("${aws.region}") String region,
                     @Value("${aws.s3.bucket}") String bucketName){
        this.accessKeyId = accessKeyId;
        this.secretKey = secretKey;
        this.region = region;
        this.bucketName = bucketName;
        this.s3Client =S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKeyId, secretKey)
                        )
                )
                .build();
    }

    public void uploadFile(MultipartFile fileBytes, String contentType, String s3key) throws IOException {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3key)
                .contentType(contentType)
                .build();
        s3Client.putObject(request, RequestBody.fromBytes(fileBytes.getBytes()));
    }

    public String generatePresignedUrl(String s3key) {
        try (
                // Create S3 presigner
                S3Presigner presigner = S3Presigner.builder()
                        .region(Region.of(region))
                        .credentialsProvider(
                                StaticCredentialsProvider.create(
                                        AwsBasicCredentials.create(accessKeyId, secretKey)
                                )
                        )
                        .build()
        )
        {
            //Define the request
            GetObjectRequest getRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3key)
                    .build();
            //Create the URL with expire
            PresignedGetObjectRequest presigned = presigner.presignGetObject(
                    builder -> builder.signatureDuration(Duration.ofMinutes(15))
                            .getObjectRequest(getRequest));
            return presigned.url().toString();
        }
    }

    public void deleteFile(String s3key){
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(s3key)
                .build();
        s3Client.deleteObject(request);
    }
}
