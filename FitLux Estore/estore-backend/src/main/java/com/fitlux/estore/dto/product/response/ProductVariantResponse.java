package com.fitlux.estore.dto.product.response;

public record ProductVariantResponse(
        Long id,
        String flavor,
        String size,
        Double price,
        Integer stock,
        String sku,
        Boolean active
) {
}
