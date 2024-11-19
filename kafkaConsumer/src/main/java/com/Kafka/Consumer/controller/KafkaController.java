package com.Kafka.Consumer.controller;

import com.Kafka.Consumer.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/message")
public class KafkaController {

    @Autowired
   private KafkaService kafkaService;
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody String value){
        boolean response = kafkaService.generateTopicMessage(value);
        return ResponseEntity.ok().body(response);
    }

}
