package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "productId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_ORDER, required = false) String sortDir,
            @RequestParam(value = "", required = false) String keyword) {
        return new ResponseEntity<>(productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir, keyword), HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "productId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_ORDER, required = false) String sortDir) {
        return new ResponseEntity<>(productService.getAllProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }


    @PostMapping("/admin/categories/{categoryId}/products")
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductDto product, @PathVariable Long categoryId) {
        return new ResponseEntity<>(productService.addProduct(product, categoryId), HttpStatus.CREATED);
    }

    @PutMapping("/admin/products/{productId}/categories/{categoryId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto product, @PathVariable Long productId, @PathVariable Long categoryId) {
        return new ResponseEntity<>(productService.updateProduct(product, productId, categoryId), HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }

    @PostMapping("/admin/products/{productId}/uploadImage")
    public ResponseEntity<ProductDto> uploadImage(@PathVariable Long productId, @RequestParam("image") MultipartFile image) {
        ProductDto productDto = productService.uploadProductImage(productId, image);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }
}
