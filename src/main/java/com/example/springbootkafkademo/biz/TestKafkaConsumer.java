package com.example.springbootkafkademo.biz;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TestKafkaConsumer {
    @KafkaListener(id = "my-test-group",
            topics = {"my-test"},
            containerFactory = "testKafkaListenerContainerFactory")
    public void consumer(List<ConsumerRecord<String, String>> records) {
        if(records != null){
            log.info("batch = {}",records.stream().map(ConsumerRecord::value).collect(Collectors.toList()));
        }
    }
}
