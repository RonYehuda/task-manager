package com.ron.taskmanager.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final String secretKey;
    private final long expiration;

    public JwtService(@Value("${jwt.secret}") String secretKey,
                      @Value("${jwt.expiration}") long expiration){
        this.secretKey = secretKey;
        this.expiration = expiration;
    }

    private SecretKey getSigningKey(){
        //Create SecretKey Obj from the field of secretKey (String)
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secretKey));
    }

    public String generateToken(String username){

        SecretKey secretKey = getSigningKey();
        // Build the token (the token's type is a String)
        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(secretKey)
                .compact();
        return token;
    }

    public String extractUsername(String token){
        SecretKey secretKey = getSigningKey();

        String username = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();

        return  username;
    }

    private Date extractExpiration(String token){
        SecretKey secretKey = getSigningKey();
        return  Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }
    public boolean isTokenValid(String token, String username){
        String extractUsername = extractUsername(token);
        Date expireDate = extractExpiration(token);
        return username.equals(extractUsername) && expireDate.after(new Date());
    }

}
