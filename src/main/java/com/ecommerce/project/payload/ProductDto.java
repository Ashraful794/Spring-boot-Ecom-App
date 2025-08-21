package com.ecommerce.project.payload;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long productId;
    @NotBlank(message = "Product name cannot be blank")
    private String productName;
    @NotBlank(message = "Description cannot be blank")
    private String description;
    private String image;
    @Min(value = 0, message = "Quantity must be zero or positive")
    private Integer quantity;
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be zero or positive")
    private double price;
    @DecimalMin(value = "0.0", inclusive = true, message = "Discount must be zero or positive")
    private double discount;
    private double specialPrice;
}
