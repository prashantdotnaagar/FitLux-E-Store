package com.fitlux.estore.dto.product.request;

import jakarta.validation.constraints.NotNull;

public record BrandStatusRequest(
        @NotNull Boolean active
) {}
