package com.rcastro.customer.management.customermanagement.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rcastro.customer.management.customermanagement.domain.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityProducer  {

    public static final String ACTIVITY = "activityTopic";

    @Autowired
    private Sender sender;

    @Autowired
    private ObjectMapper objectMapper;

    public void producer(Activity activity) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(activity);
        sender.sendMessage(message, ACTIVITY);
    }
}
