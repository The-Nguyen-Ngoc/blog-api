package com.example.blogapi.service;

import com.example.blogapi.dto.request.EmailDetails;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MyProducer {

    private final KafkaTemplate<String, EmailDetails> kafkaTemplate;

    public MyProducer(KafkaTemplate<String, EmailDetails> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, EmailDetails message) {
        kafkaTemplate.send(topic, message);

    }
}