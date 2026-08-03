package com.notificationservice.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.notificationservice.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    // Create signing key from JWT secret
    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)
        );
    }

    // Extract all claims
    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract username (subject)
    public String extractUsername(String token) {

        return extractAllClaims(token).getSubject();
    }

    // Extract role
    public String extractRole(String token) {

        return extractAllClaims(token)
                .get("role", String.class);
    }

    // Extract expiration
    public Date extractExpiration(String token) {

        return extractAllClaims(token).getExpiration();
    }

    // Check token expiry
    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    // Validate JWT
    public boolean isTokenValid(String token) {

        try {

            System.out.println("Checking JWT...");

            Claims claims = extractAllClaims(token);

            System.out.println("Subject    : " + claims.getSubject());
            System.out.println("Role       : " + claims.get("role"));
            System.out.println("Expiration : " + claims.getExpiration());
            System.out.println("Current    : " + new Date());

            return !isTokenExpired(token);

        } catch (Exception e) {

            System.out.println("JWT Exception: " + e.getClass().getSimpleName());
            System.out.println("JWT Exception: " + e.getClass().getName());
            e.printStackTrace();

            return false;
        }
    }
}