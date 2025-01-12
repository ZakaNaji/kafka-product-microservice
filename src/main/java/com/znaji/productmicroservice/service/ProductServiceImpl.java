package com.znaji.productmicroservice.service;

import com.znaji.productmicroservice.dto.ProductDTO;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    @Value("${product.created.kafka.topic.name}")
    private String topicName;
    private Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public String createProduct(ProductDTO productDTO) throws ExecutionException, InterruptedException {
        String productId = UUID.randomUUID().toString();
        ProductCreatedEvent productCreatedEvent = ProductCreatedEvent.builder()
                .productId(productId)
                .title(productDTO.getTitle())
                .price(productDTO.getPrice())
                .quantity(productDTO.getQuantity())
                .build();

        //TODO: persiste the product to a DB

        //Send kafka event:

        LOGGER.info("**** Before event: ");
        SendResult<String, ProductCreatedEvent> result = kafkaTemplate
                .send(topicName, productId, productCreatedEvent)
                .get();

        LOGGER.info("topic: " + result.getRecordMetadata().topic());
        LOGGER.info("partition: " + result.getRecordMetadata().partition());

        LOGGER.info("****** After event.");
        return productId;
    }
}
