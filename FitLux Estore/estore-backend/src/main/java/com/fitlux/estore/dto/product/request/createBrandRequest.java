package com.fitlux.estore.dto.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record createBrandRequest(
        @NotBlank(message = "Brand name is required")
        @Size(min = 2, max = 100, message = "Brand name must be between 2 and 100 characters")
        String name,

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description
) {}
