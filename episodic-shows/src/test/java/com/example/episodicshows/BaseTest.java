package com.example.episodicshows;

import org.junit.AfterClass;
import org.junit.ClassRule;
import org.springframework.amqp.rabbit.junit.BrokerRunning;

public class BaseTest {
    @ClassRule
    public static BrokerRunning brokerRunning = BrokerRunning.isRunningWithEmptyQueues("episodic-progress");

    @AfterClass
    public static void tearDown() {
        brokerRunning.removeTestQueues();
    }
}

