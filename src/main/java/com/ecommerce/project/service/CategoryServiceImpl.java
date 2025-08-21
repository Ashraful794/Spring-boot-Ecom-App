package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDto;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;


    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = Sort.by(sortBy);
        sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? sortByAndOrder.ascending() : sortByAndOrder.descending();

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageRequest);

        List<CategoryDto> categoryDtos = categoryPage.getContent().stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .toList();

        if (categoryDtos.isEmpty()) {
            throw new APIException("No categories found.");
        }

        return new CategoryResponse(
                categoryDtos,
                categoryPage.getNumber(),
                categoryPage.getSize(),
                categoryPage.getTotalElements(),
                categoryPage.getTotalPages(),
                categoryPage.isLast()
        );
    }

    @Override
    public CategoryDto createCategory(CategoryDto category) {
        if (categoryRepository.findByCategoryName(category.getCategoryName()) != null) {
            throw new APIException("Category with name " + category.getCategoryName() + " already exists.");
        }
        Category categoryEntity = modelMapper.map(category, Category.class);
        Category savedCategory = categoryRepository.save(categoryEntity);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id, "category Id"));
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto category, Long categoryId) {
        Category updateCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId, "category Id"));
        updateCategory.setCategoryName(category.getCategoryName());
        return modelMapper.map(categoryRepository.save(updateCategory), CategoryDto.class);
    }
}
