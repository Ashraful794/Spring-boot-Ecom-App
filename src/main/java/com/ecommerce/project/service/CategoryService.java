package com.ecommerce.project.service;


import com.ecommerce.project.payload.CategoryDto;
import com.ecommerce.project.payload.CategoryResponse;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder);

    CategoryDto createCategory(CategoryDto category);

    CategoryDto deleteCategory(Long id);

    CategoryDto updateCategory(CategoryDto category, Long categoryId);
}
