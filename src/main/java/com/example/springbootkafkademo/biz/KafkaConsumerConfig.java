package com.example.springbootkafkademo.biz;

import lombok.Data;

@Data
public class KafkaConsumerConfig {
    private String domain;
    private String topic;
    private Integer partition;
    private String groupId;
    private Integer sessionTimeout;
    private Integer maxPoolRecords;
    private Integer maxBytes;
}
