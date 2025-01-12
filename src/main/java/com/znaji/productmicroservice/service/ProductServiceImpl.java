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

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    @Value("${product.created.kafka.topic.name}")
    private String topicName;
    private Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public String createProduct(ProductDTO productDTO) {
        String productId = UUID.randomUUID().toString();
        ProductCreatedEvent productCreatedEvent = ProductCreatedEvent.builder()
                .productId(productId)
                .title(productDTO.getTitle())
                .price(productDTO.getPrice())
                .quantity(productDTO.getQuantity())
                .build();

        //TODO: persiste the product to a DB

        //Send kafka event:

        CompletableFuture<SendResult<String, ProductCreatedEvent>> event = kafkaTemplate.send(topicName, productId, productCreatedEvent);
        event.whenComplete((result, exception) -> {
            if (exception != null) {
                LOGGER.error("****** Error while sending the event: " + exception.getMessage());
            } else {
                LOGGER.info("****** Event sent successfully: " + result.getRecordMetadata());
            }
        });
        //to send the kafka event sync you need to block this thread like this:
        //event.join();
        //but for now will be sending it in a non-blocking way:
        LOGGER.info("****** Product id sent successfully.");
        return productId;
    }
}
