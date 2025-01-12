package com.znaji.productmicroservice.config;

import com.znaji.productmicroservice.service.ProductCreatedEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${product.created.kafka.topic.name}")
    private String topicName;
    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServer;
    @Value("${spring.kafka.producer.acks}")
    private String all;
    @Value("${spring.kafka.producer.retries}")
    private int retries;
    @Value("${spring.kafka.producer.properties.retry.backoff.ms}")
    private String backoff;

    @Bean
    public KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate() {
        return new KafkaTemplate<String, ProductCreatedEvent>(producerFactory());
    }
    @Bean
    public ProducerFactory<String, ProductCreatedEvent> producerFactory() {
        return new DefaultKafkaProducerFactory<String, ProductCreatedEvent>(kafkaProducerConfig());
    }

    private Map<String, Object> kafkaProducerConfig() {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName(),
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName(),
                ProducerConfig.ACKS_CONFIG, all,
                ProducerConfig.RETRIES_CONFIG, retries,
                ProducerConfig.RETRY_BACKOFF_MS_CONFIG, backoff
        );
    }

    @Bean
    public NewTopic newTopic() {
        return TopicBuilder.name(topicName)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }
}
