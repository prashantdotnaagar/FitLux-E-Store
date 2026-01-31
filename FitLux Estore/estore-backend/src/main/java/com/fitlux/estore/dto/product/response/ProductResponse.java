package com.fitlux.estore.dto.product.response;

public record ProductResponse(
        Long id,
        String name,
        BrandResponse brand,
        CategoryResponse category,
        String description,
        Boolean active
) {
}
