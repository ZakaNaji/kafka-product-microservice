package com.znaji.productmicroservice.service;

import com.znaji.productmicroservice.dto.ProductDTO;

import java.util.concurrent.ExecutionException;

public interface ProductService {
    String createProduct(ProductDTO productDTO) throws ExecutionException, InterruptedException;
}
