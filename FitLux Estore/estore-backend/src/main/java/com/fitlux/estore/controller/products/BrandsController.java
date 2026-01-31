package com.fitlux.estore.controller.products;

import com.fitlux.estore.constants.ApiResponse;
import com.fitlux.estore.constants.serviceCodes.enums.serviceCodeImpl;
import com.fitlux.estore.controller.auth.AuthController;
import com.fitlux.estore.dto.product.request.BrandStatusRequest;
import com.fitlux.estore.dto.product.request.UpdateBrandRequest;
import com.fitlux.estore.dto.product.request.createBrandRequest;
import com.fitlux.estore.dto.product.response.BrandResponse;
import com.fitlux.estore.service.products.BrandService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/brands")
public class BrandsController {
    private static final Logger logger = LoggerFactory.getLogger(BrandsController.class);

    private final BrandService brandService;

    public BrandsController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping("/create-brand")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('BRAND_MANAGE')")
    ResponseEntity<ApiResponse<BrandResponse>> createBrand(@RequestBody createBrandRequest request){
        logger.info("Received brand creation request");
        BrandResponse response=brandService.createBrand(request);
        logger.info("Brand create request processed successfully");
        return ResponseEntity.ok(ApiResponse.success(serviceCodeImpl.BRAND_CREATED, response));
    }

    @PutMapping("/{brandId}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('BRAND_MANAGE')")
    public ResponseEntity<ApiResponse<BrandResponse>> updateBrand(
            @PathVariable Long brandId,
            @Valid @RequestBody UpdateBrandRequest request
    ) {
        BrandResponse response =brandService.updateBrand(brandId, request);
        return ResponseEntity.ok(ApiResponse.success(serviceCodeImpl.BRAND_UPDATED_SUCCESS,response));
    }

    @DeleteMapping("/{brandId}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('BRAND_MANAGE')")
    public ResponseEntity<ApiResponse<Long>> deleteBrand(@PathVariable Long brandId) {

        Long deletedBrandId = brandService.deleteBrand(brandId);

        return ResponseEntity.ok(
                ApiResponse.success(serviceCodeImpl.BRAND_DELETED_SUCCESS, deletedBrandId)
        );
    }

    @PatchMapping("/{brandId}/status")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('BRAND_MANAGE')")
    public ResponseEntity<ApiResponse<BrandResponse>> updateBrandStatus(
            @PathVariable Long brandId,
            @Valid @RequestBody BrandStatusRequest request
    ) {
        BrandResponse response = brandService.updateBrandStatus(brandId, request.active());

        return ResponseEntity.ok(
                ApiResponse.success(serviceCodeImpl.BRAND_STATUS_UPDATED_SUCCESS, response)
        );
    }


}
