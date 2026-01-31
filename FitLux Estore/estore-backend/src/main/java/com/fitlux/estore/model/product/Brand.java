package com.fitlux.estore.model.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "brands")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private Boolean active = true;
}
