package com.fitlux.estore.service.products;

import com.fitlux.estore.common.exception.BusinessException;
import com.fitlux.estore.constants.serviceCodes.enums.serviceCodeImpl;
import com.fitlux.estore.dto.product.request.UpdateBrandRequest;
import com.fitlux.estore.dto.product.request.createBrandRequest;
import com.fitlux.estore.dto.product.response.BrandResponse;
import com.fitlux.estore.model.product.Brand;
import com.fitlux.estore.repository.products.BrandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BrandService {
    private static final Logger logger = LoggerFactory.getLogger(BrandService.class);
    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Transactional
    public BrandResponse createBrand(createBrandRequest request) {
        logger.info("Attempting to create brand: {}", request.name());

        if (brandRepository.existsByName(request.name())) {
            logger.warn("Brand creation failed: Brand already exists: {}", request.name());
            throw new BusinessException(serviceCodeImpl.BRAND_ALREADY_EXISTS);
        }

        Brand brand = Brand.builder()
                .name(request.name())
                .description(request.description())
                .active(true)
                .build();

        Brand savedBrand = brandRepository.save(brand);
        logger.info("Brand created successfully: {}", savedBrand.getName());

        return new BrandResponse(
                savedBrand.getId(),
                savedBrand.getName(),
                savedBrand.getDescription(),
                savedBrand.getActive()
        );
    }
    @Transactional
    public BrandResponse updateBrand(Long brandId, UpdateBrandRequest request) {

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BusinessException(serviceCodeImpl.BRAND_NOT_FOUND));

        if (request.name() != null && !request.name().isBlank()) {
            brand.setName(request.name());
        }

        if (request.description() != null) {
            brand.setDescription(request.description());
        }

        Brand updated = brandRepository.save(brand);

        return new BrandResponse(
                updated.getId(),
                updated.getName(),
                updated.getDescription(),
                updated.getActive()
        );
    }

    @Transactional
    public Long deleteBrand(Long brandId) {

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BusinessException(serviceCodeImpl.BRAND_NOT_FOUND));

        brandRepository.delete(brand);

        return brandId;
    }

    @Transactional
    public BrandResponse updateBrandStatus(Long brandId, Boolean active) {

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BusinessException(serviceCodeImpl.BRAND_NOT_FOUND));

        brand.setActive(active);

        Brand updated = brandRepository.save(brand);

        return new BrandResponse(
                updated.getId(),
                updated.getName(),
                updated.getDescription(),
                updated.getActive()
        );
    }


}
