package com.ron.taskmanager.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@EnableAsync
@Service
public class EmailNotificationService {
    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationService.class);

    @Async
    public void sendTaskCreatedNotification(String userEmail,String taskTitle){
        logger.info("Sending email to: {} |Thread: {} Task: {} "
                ,userEmail,Thread.currentThread().getName(),taskTitle);
        try {
            Thread.sleep(2000);
            logger.info("Email sent successfully to: {}",userEmail);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            logger.error("Failed to send email to: {} - Thread interrupted", userEmail);
        }
    }
}
