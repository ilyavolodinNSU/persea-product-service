package ru.persea.productservice.entity.factor;

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
import ru.persea.productservice.entity.product.CategoryEntity;

@Entity
@Table(name = "factor_enum_rules")
@Getter
@Setter
@NoArgsConstructor
public class FactorEnumRuleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enum_value_id")
    private FactorEnumValueEntity enumValue;

    @Column(name = "impact", nullable = false)
    private Integer impact;
}
