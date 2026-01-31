package com.fitlux.estore.dto.product.response;

public record BrandResponse(
        Long id,
        String name,
        String description,
        Boolean active
) {
}
