package com.ecommerce.project.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long categoryId;

    @NotBlank(message = "Category name cannot be blank")
    @Size(min = 5, message = "Category name must be at least 5 characters long")
    private String categoryName;
}
