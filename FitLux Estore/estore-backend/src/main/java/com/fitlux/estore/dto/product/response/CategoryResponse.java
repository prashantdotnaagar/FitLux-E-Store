package com.fitlux.estore.dto.product.response;

public record CategoryResponse(
        Long id,
        String name,
        String description,
        Boolean active
) {
}
