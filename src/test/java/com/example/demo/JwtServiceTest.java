package com.example.demo;

import com.example.demo.config.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private static final String SECRET_KEY = "E6F886CFBEB51E489C29111DF69277B05417FC4002EA2C8571F0E61C9A74C98F";
    @InjectMocks
    private JwtService jwtService;

    @Test
    void testExtractUsername() {
        String token = generateToken("testUser");

        String extractedUsername = jwtService.extractUsername(token);

        assertEquals("testUser", extractedUsername);
    }

    @Test
    void testGenerateToken() {
        UserDetails userDetails = new User("testUser", "password", new ArrayList<>());

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testIsTokenValid() {

        UserDetails userDetails = new User("testUser", "password", new ArrayList<>());
        String validToken = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(validToken, userDetails));
    }

    @Test
    void testIsTokenValidWithExpiredToken() {
        UserDetails userDetails = new User("testUser", "password", new ArrayList<>());
        String expiredToken = generateExpiredToken("testUser");

        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(expiredToken, userDetails));
    }


    private String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return jwtService.generateToken(claims, new User(username, "", new ArrayList<>()));
    }

    private String generateExpiredToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000)) // Set issued date to one second ago
                .setExpiration(new Date(System.currentTimeMillis() - 500)) // Set expiration date to 500 milliseconds ago
                .signWith(SignatureAlgorithm.HS256, getSignInKey())
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
