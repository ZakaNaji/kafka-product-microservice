package com.znaji.productmicroservice.controller;

import com.znaji.productmicroservice.dto.ProductDTO;
import com.znaji.productmicroservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductDTO productDTO) throws ExecutionException, InterruptedException {

        String productId = productService.createProduct(productDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productId);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse error = ErrorResponse
                .builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, "Fail to create Kafka event")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
 }
