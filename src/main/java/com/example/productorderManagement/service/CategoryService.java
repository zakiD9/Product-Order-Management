package com.example.productorderManagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.productorderManagement.dto.CategoryDTO;
import com.example.productorderManagement.exception.BadRequestException;
import com.example.productorderManagement.exception.ResourceNotFoundException;
import com.example.productorderManagement.model.Category;
import com.example.productorderManagement.repository.CategoryRepository;
import com.example.productorderManagement.repository.ProductRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository,ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository =productRepository;
    }

    public CategoryDTO createCategory(Category category) {
        boolean exists = categoryRepository.existsByName(category.getName());
        if (exists) {
            throw new BadRequestException("Category with this name already exists");
        }
        Category saved = categoryRepository.save(category);
        return new CategoryDTO(saved);
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::new)
                .toList();
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return new CategoryDTO(category);
    }

    public CategoryDTO updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());

        Category updated = categoryRepository.save(category);
        return new CategoryDTO(updated);
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
