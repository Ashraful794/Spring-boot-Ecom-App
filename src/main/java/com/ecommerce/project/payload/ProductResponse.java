package com.ecommerce.project.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse extends ListContentDto {
    private List<ProductDto> content;
    public ProductResponse(List<ProductDto> content, Integer pageNumber, Integer pageSize, Long totalElements, Integer totalPages, Boolean lastPage) {
        super(pageNumber, pageSize, totalElements, totalPages, lastPage);
        this.content = content;
    }
}
