package com.example.episodicshows.config;

import com.example.episodicshows.viewings.ProgressMessage;
import com.example.episodicshows.viewings.ViewingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
public class AmqpListener implements RabbitListenerConfigurer {
    @Autowired
    private AppConfig appConfig;

    private final ViewingService viewingService;

    public AmqpListener(ViewingService viewingService) {
        this.viewingService = viewingService;
    }

    @RabbitListener(queues = "${progress-queue}")
    public void receiveMessage(final ProgressMessage message) {
//        System.out.println("************************************************");
//        System.out.println("message.getUserId() = " + message.getUserId());
//        System.out.println("message.getEpisodeId() = " + message.getEpisodeId());
//        System.out.println("message.getCreatedAt() = " + message.getCreatedAt());
//        System.out.println("message.getOffset() = " + message.getOffset());
//        System.out.println("************************************************");
        viewingService.updateProgress(message);
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(messageConverter());
        return factory;
    }

    @Bean
    public MappingJackson2MessageConverter messageConverter() {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setObjectMapper(appConfig.getObjectMapper());
        return messageConverter;
    }

    @Override
    public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }
}
