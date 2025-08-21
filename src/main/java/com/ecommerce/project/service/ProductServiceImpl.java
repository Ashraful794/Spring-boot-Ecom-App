package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper, FileService fileService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
    }


    @Override
    public ProductDto addProduct(ProductDto productDto, Long categoryId) {


        if (productRepository.findByProductName(productDto.getProductName()) != null) {
            throw new APIException("Product with name '" + productDto.getProductName() + "' already exists.");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId, "category Id"));

        Product product = modelMapper.map(productDto, Product.class);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDto.class);
    }


    @Override
    public ProductResponse getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir, String keyword) {
        return getProductsResponse(null, keyword, pageNumber, pageSize, sortBy, sortDir);
    }

    @Override
    public ProductResponse getAllProductsByCategory(Long categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Validate category existence
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId, "category Id"));
        return getProductsResponse(category, null, pageNumber, pageSize, sortBy, sortDir);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, Long productId, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId, "category Id"));

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", productId, "product Id"));


        Product updatedProduct = modelMapper.map(productDto, Product.class);
        updatedProduct.setProductId(product.getProductId());
        updatedProduct.setCategory(category);

        return modelMapper.map(productRepository.save(updatedProduct), ProductDto.class);
    }

    @Override
    public ProductDto deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", productId, "product Id"));
        productRepository.delete(product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto uploadProductImage(Long productId, MultipartFile image) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", productId, "product Id"));


        String fileName = fileService.uploadImage(path, image);

        product.setImage(fileName);


        return modelMapper.map(productRepository.save(product), ProductDto.class);
    }


    private ProductResponse getProductsResponse(Category category, String keyword, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = Sort.by(sortBy);
        sort = sortDir.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> products;
        if (category == null && (keyword == null || keyword.isEmpty())) {
            products = productRepository.findAll(pageRequest);
        } else if (keyword != null && !keyword.isEmpty()) {
            products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%', pageRequest);
        } else {
            products = productRepository.findByCategory(category, pageRequest);
        }

        List<ProductDto> productDtos = products.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();

        if (productDtos.isEmpty()) {
            throw new APIException("No products found.");
        }

        return new ProductResponse(
                productDtos,
                products.getNumber(),
                products.getSize(),
                products.getTotalElements(),
                products.getTotalPages(),
                products.isLast()
        );
    }


}
