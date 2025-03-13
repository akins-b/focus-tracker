package com.boma.focus.tracker.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JWTService {

    private static final String SECRET_FILE_PATH = "src/main/resources/.jwt_secret";
    private final SecretKey secretKey;


    public JWTService() {
        try {
            this.secretKey = loadOrGenerateSecretKey();
        } catch (Exception e) {
            throw new RuntimeException("Error loading/generating the JWT secret key", e);
        }
    }

    private SecretKey loadOrGenerateSecretKey(){

        try{
            File file = new File(SECRET_FILE_PATH);

            if (file.exists()) {
                String keyString = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8).trim();
                byte [] keyBytes = Decoders.BASE64.decode(keyString);
                return Keys.hmacShaKeyFor(keyBytes);
            }
            else {
                SecretKey newKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
                String encodedKey = Base64.getEncoder().encodeToString(newKey.getEncoded());

                Files.write(file.toPath(), encodedKey.getBytes(), StandardOpenOption.CREATE);
                return newKey;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading/writing the JWT secret key file", e);
        }
    }


    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (1000 *60 * 60 * 30)))
                .and()
                .signWith(secretKey)
                .compact();
    }



    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
