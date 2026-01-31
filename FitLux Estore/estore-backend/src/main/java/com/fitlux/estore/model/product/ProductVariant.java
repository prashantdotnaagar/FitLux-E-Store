package com.fitlux.estore.model.product;

import jakarta.persistence.*;

@Entity
@Table(name = "product_variants")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String flavor;     // Chocolate, Mango, NA
    private String size;       // 1kg, 2kg, M, L

    private Double price;
    private Integer stock;

    @Column(nullable = false, unique = true)
    private String sku;

    private Boolean active = true;
}
