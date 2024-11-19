package com.Kafka.Consumer.service;

import com.Kafka.Consumer.config.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    @Autowired
    public KafkaTemplate<String,String> kafkaTemplate;

    public boolean generateTopicMessage(String value){
        this.kafkaTemplate.send(AppConstants.TOPIC_NAME,value);
        return true;
    }

}
