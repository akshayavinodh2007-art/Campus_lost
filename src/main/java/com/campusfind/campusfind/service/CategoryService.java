package com.campusfind.campusfind.service;

import com.campusfind.campusfind.entity.Category;
import com.campusfind.campusfind.exception.BusinessRuleViolationException;
import com.campusfind.campusfind.exception.ResourceNotFoundException;
import com.campusfind.campusfind.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {
        if (categoryRepository.existsByNameIgnoreCase(category.getName().trim())) {
            throw new BusinessRuleViolationException("Category '" + category.getName() + "' already exists");
        }
        category.setName(category.getName().trim());
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
}
