package com.fitlux.estore.dto.product.response;

import java.util.List;

public record ProductWithVariantsResponse(
        Long id,
        String name,
        BrandResponse brand,
        CategoryResponse category,
        String description,
        Boolean active,
        List<ProductVariantResponse> variants
) {
}
