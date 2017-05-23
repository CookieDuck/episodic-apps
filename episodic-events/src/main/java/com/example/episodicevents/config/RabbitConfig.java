package com.example.episodicevents.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(value = "progress")
@Getter
@Setter
public class RabbitConfig {
    private String exchange;
    private String queue;
    private String key;
}
