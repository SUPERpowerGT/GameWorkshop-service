package com.gameworkshop.infrastructure.security;

import com.gameworkshop.infrastructure.config.KeyConfig;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final KeyConfig keyConfig;

    public JwtTokenProvider(KeyConfig keyConfig) {
        this.keyConfig = keyConfig;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(keyConfig.getPublicKey()) //public key check
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Token failed: " + e.getMessage());
            return false;
        }
    }

    public String getUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(keyConfig.getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        Object uidClaim = claims.get("uid");
        if (uidClaim != null) {
            return uidClaim.toString();
        }
        return claims.getSubject();
    }
}
