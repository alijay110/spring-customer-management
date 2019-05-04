package com.rcastro.customer.management.customermanagement.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rcastro.customer.management.customermanagement.domain.Activity;
import com.rcastro.customer.management.customermanagement.service.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EventConsumer {

    private static Logger log = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "activityTopic", groupId = "customerGroup")
    public void listen(String message) {
        log.info("Received message in customerGroup: " + message);
        try {
            Activity activity = objectMapper.readValue(message, Activity.class);
            activityService.save(activity);
        } catch (IOException e) {
            log.error("Error to read message.");
        }
    }
}
