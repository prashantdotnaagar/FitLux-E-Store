package com.fitlux.estore.dto.product.request;

public record CreateProductRequest(
        String name,
        Long brandId,
        Long categoryId,
        String description
) {
}