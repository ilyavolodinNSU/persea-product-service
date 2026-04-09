package ru.persea.productservice.entity;

import java.time.Instant;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;
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
@Setting(settingPath = "elasticsearch/product-settings.json")
public class ProductDocument {
    @Id
    private Long id;

    @MultiField(
        mainField = @Field(type = FieldType.Text, analyzer = "standard"),
        otherFields = {
            @InnerField(suffix = "autocomplete", type = FieldType.Text, analyzer = "autocomplete", searchAnalyzer = "autocomplete_search")
        }
    )
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
