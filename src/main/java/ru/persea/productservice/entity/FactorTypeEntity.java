package ru.persea.productservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "factor_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FactorTypeEntity {
    @Id
    private Integer id;

    private String name;
}
