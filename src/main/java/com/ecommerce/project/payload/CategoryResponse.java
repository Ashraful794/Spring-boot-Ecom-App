package com.ecommerce.project.payload;


import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse extends ListContentDto {
    private List<CategoryDto> content;

    public CategoryResponse(List<CategoryDto> content, Integer pageNumber, Integer pageSize, Long totalElements, Integer totalpages, Boolean lastPage) {
        super(pageNumber, pageSize, totalElements, totalpages, lastPage);
        this.content = content;
    }
}
