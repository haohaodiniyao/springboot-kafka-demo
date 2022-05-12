package com.example.springbootkafkademo.biz;

import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * 消费者
 */
@Configuration
@ConfigurationProperties(prefix = "kafka.consumers")
@Data
public class KafkaConsumerConfiguration {
    private KafkaConsumerConfig test;
    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> testKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        /**
         * 消费者组 my-test-group
         * topic有5个分区
         * 2022-02-22 20:28:58.449  INFO 15640 --- [est-group-2-C-1] o.s.k.l.KafkaMessageListenerContainer    : my-test-group: partitions assigned: [my-test-4]
         * 2022-02-22 20:28:58.449  INFO 15640 --- [est-group-1-C-1] o.s.k.l.KafkaMessageListenerContainer    : my-test-group: partitions assigned: [my-test-2, my-test-3]
         * 2022-02-22 20:28:58.449  INFO 15640 --- [est-group-0-C-1] o.s.k.l.KafkaMessageListenerContainer    : my-test-group: partitions assigned: [my-test-1, my-test-0]
         */
        //消费者数量
        factory.setConcurrency(1);
        factory.setBatchListener(true);
        //默认5000ms
        factory.getContainerProperties().setPollTimeout(5000);
        return factory;
    }
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, test.getDomain());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, test.getGroupId());
        //解码
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        // value.deserializer
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        // session.timeout.ms
//        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, test.getSessionTimeout());
        // max.poll.records
//        properties.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, test.getMaxBytes());
        //如果为真，消费者的偏移量将在后台定期提交
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 5000);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, test.getMaxPoolRecords());
        return properties;
    }
}
