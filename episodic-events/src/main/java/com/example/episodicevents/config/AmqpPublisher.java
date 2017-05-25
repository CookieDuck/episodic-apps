package com.example.episodicevents.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpPublisher {
    @Autowired
    ObjectMapper objectMapper;

    private final RabbitConfig config;

    public AmqpPublisher(RabbitConfig config) {
        this.config = config;
    }

    @Bean
    public TopicExchange appExchange() {
        return new TopicExchange(config.getExchange());
    }

    @Bean
    public Queue queue() {
        return new Queue(config.getQueue());
    }

    @Bean
    public Binding declareBinding() {
        return BindingBuilder.bind(queue()).to(appExchange()).with(config.getKey());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter(objectMapper);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
