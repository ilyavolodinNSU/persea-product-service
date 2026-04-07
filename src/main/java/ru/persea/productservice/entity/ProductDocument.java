package ru.persea.productservice.entity;

import java.time.Instant;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "products")
public class ProductDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Integer, name = "category_id")
    private Integer categoryId;

    @Field(type = FieldType.Integer, name = "brand_id")
    private Integer brandId;

    @Field(type = FieldType.Integer, name = "rating")
    private Integer rating;

    @Field(type = FieldType.Text, name = "image_uri")
    private String imageURI;

    @Field(type = FieldType.Date, name = "updated_at", pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSSSSZZZZZ")
    private Instant updatedAt;
}
