package com.fitlux.estore.dto.product.request;

public record CreateProductVariantRequest(
        String flavor,
        String size,
        Double price,
        Integer stock,
        String sku
) {
}
