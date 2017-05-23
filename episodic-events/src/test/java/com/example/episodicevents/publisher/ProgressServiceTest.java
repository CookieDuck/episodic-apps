package com.example.episodicevents.publisher;

import com.example.episodicevents.BaseTest;
import com.example.episodicevents.config.RabbitConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class ProgressServiceTest extends BaseTest {
    private ProgressService service;

    @Mock
    RabbitTemplate rabbitTemplate;

    RabbitConfig config;

    @Before
    public void init() {
        config = new RabbitConfig();
        config.setExchange("blah blah exchange somethingorother");
        config.setKey("blah blah key");
        config.setQueue("blah queue blah");
        service = new ProgressService(rabbitTemplate, config);
    }

    @Test
    public void testMessageGetsPublishedToQueue() {
        service.sendMessage(new ProgressMessage(1L, 2L, LocalDateTime.now(), 123));

        verify(rabbitTemplate).convertAndSend(
                eq(config.getExchange()),
                eq(config.getKey()),
                any(ProgressService.class));
    }
}
