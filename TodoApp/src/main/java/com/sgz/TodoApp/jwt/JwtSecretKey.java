package com.sgz.TodoApp.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtSecretKey {

    private final JwtConfig config;

    @Autowired
    public JwtSecretKey(JwtConfig config) {
        this.config = config;
    }

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(config.getSecretKey().getBytes());
    }

}
