package com.example.catalog.services;

import com.example.catalog.dto.CategoryDTO;
import com.example.catalog.repositories.CategoryRepository;
import com.example.catalog.services.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream().map(CategoryDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        return categoryRepository.findById(id).map(CategoryDTO::new).orElseThrow(
                () -> new EntityNotFoundException("Category not found"));
    }
}
