package com.example.episodicevents.publisher;

import com.example.episodicevents.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProgressService {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitConfig rabbitConfig;

    public ProgressService(RabbitTemplate rabbitTemplate, RabbitConfig rabbitConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitConfig = rabbitConfig;
    }

    public void sendMessage(ProgressMessage message) {
        rabbitTemplate.convertAndSend(rabbitConfig.getExchange(), rabbitConfig.getKey(), message);
    }
}
