package ru.persea.productservice.entity.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.persea.productservice.entity.factor.FactorEntity;
import ru.persea.productservice.entity.factor.FactorEnumValueEntity;

@Entity
@Table(name = "product_enum_factors")
@Getter
@Setter
@NoArgsConstructor
public class ProductEnumFactorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factor_id")
    private FactorEntity factor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enum_value_id")
    private FactorEnumValueEntity enumValue;
}
