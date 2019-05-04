package com.rcastro.customer.management.customermanagement.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public final class Sender {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String msg, String topicName) {
        kafkaTemplate.send(topicName, msg);
    }
}
