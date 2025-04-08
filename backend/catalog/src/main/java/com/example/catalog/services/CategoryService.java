package com.example.catalog.services;

import com.example.catalog.dto.CategoryDTO;
import com.example.catalog.entities.Category;
import com.example.catalog.repositories.CategoryRepository;
import com.example.catalog.services.exceptions.DatabaseException;
import com.example.catalog.services.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(CategoryDTO::new);
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        return categoryRepository.findById(id).map(CategoryDTO::new).orElseThrow(
                () -> new ResourceNotFoundException("Category not found"));
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        return new CategoryDTO(categoryRepository.save(new Category(dto.getId(), dto.getName())));
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try {
            Category entity = categoryRepository.getReferenceById(id);
            entity.setName(dto.getName());
            return new CategoryDTO(categoryRepository.save(entity));
        } catch (jakarta.persistence.EntityNotFoundException e) {
            throw new ResourceNotFoundException("Category not found");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found");
        }
        try {
            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }
}
