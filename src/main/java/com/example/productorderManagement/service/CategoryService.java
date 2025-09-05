package com.example.productorderManagement.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.productorderManagement.dto.request.CategoryRequest;
import com.example.productorderManagement.dto.response.CategoryResponse;
import com.example.productorderManagement.exception.BadRequestException;
import com.example.productorderManagement.exception.ResourceNotFoundException;
import com.example.productorderManagement.model.Category;
import com.example.productorderManagement.repository.CategoryRepository;
import com.example.productorderManagement.repository.ProductRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        boolean exists = categoryRepository.existsByName(categoryRequest.getName());
        if (exists) {
            throw new BadRequestException("Category with this name already exists");
        }
        Category category = modelMapper.map(categoryRequest, Category.class);
        categoryRepository.save(category);
        return new CategoryResponse(category);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::new)
                .toList();
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return new CategoryResponse(category);
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest categoryDetails) {
        Boolean categoryExist = categoryRepository.existsById(id);
        if (!categoryExist) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        boolean nameExists = categoryRepository.existsByName(categoryDetails.getName());
        if (nameExists) {
            throw new BadRequestException("Category with this name already exists");
        }

        Category category = modelMapper.map(categoryDetails, Category.class);
        return new CategoryResponse(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

                boolean hasProducts = productRepository.existsByCategory(category);
                if(hasProducts){
                    throw new BadRequestException("this category still have products");
                }
                categoryRepository.delete(category);
    }
}
