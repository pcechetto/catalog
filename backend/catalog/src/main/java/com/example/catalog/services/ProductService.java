package com.example.catalog.services;

import com.example.catalog.dto.ProductDTO;
import com.example.catalog.entities.Product;
import com.example.catalog.repositories.ProductRepository;
import com.example.catalog.services.exceptions.DatabaseException;
import com.example.catalog.services.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest).map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        return productRepository.findById(id).map(entity -> new ProductDTO(entity, entity.getCategories())).orElseThrow(
                () -> new ResourceNotFoundException("Product not found"));
    }

//    @Transactional
//    public ProductDTO insert(ProductDTO dto) {
//        return new ProductDTO(productRepository.save(new ProductDTO(
//                dto.getId(), dto.getName(), dto.getDescription(), dto.getPrice(),
//                dto.getImgUrl(), dto.getDate(), dto.getCategories())));
//    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = productRepository.getReferenceById(id);
            entity.setName(dto.getName());
            return new ProductDTO(productRepository.save(entity));
        } catch (jakarta.persistence.EntityNotFoundException e) {
            throw new ResourceNotFoundException("Product not found");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }
}
