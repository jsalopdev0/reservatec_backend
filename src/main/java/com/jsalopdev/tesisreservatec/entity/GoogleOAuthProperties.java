package com.jsalopdev.tesisreservatec.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "security.oauth2.google")
@Data
public class GoogleOAuthProperties {
    private List<String> clientIds;  // Lista de client IDs
}
