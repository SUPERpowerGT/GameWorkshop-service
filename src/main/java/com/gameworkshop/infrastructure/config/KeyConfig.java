package com.gameworkshop.infrastructure.config;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;
import java.util.Base64;

@Component
public class KeyConfig {

    private PublicKey publicKey;

    @PostConstruct
    public void loadKey() throws Exception {
        //get from
        String key = new String(Files.readAllBytes(Paths.get("src/main/resources/keys/rsa-public.pem")));

        key = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.publicKey = keyFactory.generatePublic(keySpec);

        System.out.println("successfully loaded public key");
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
