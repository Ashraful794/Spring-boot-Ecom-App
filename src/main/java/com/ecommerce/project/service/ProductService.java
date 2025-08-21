package com.ecommerce.project.service;

import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ProductDto addProduct(ProductDto productDto, Long categoryId);

    ProductResponse getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir, String keyword);

    ProductResponse getAllProductsByCategory(Long categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);

    ProductDto updateProduct(ProductDto productDto, Long productId, Long categoryId);

    ProductDto deleteProduct(Long productId);

    ProductDto uploadProductImage(Long productId, MultipartFile image);
}
