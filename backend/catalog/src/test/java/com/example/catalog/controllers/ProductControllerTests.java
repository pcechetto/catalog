package com.example.catalog.controllers;

import com.example.catalog.dto.ProductDTO;
import com.example.catalog.services.ProductService;
import com.example.catalog.services.exceptions.DatabaseException;
import com.example.catalog.services.exceptions.ResourceNotFoundException;
import com.example.catalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = ProductController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService service;

    private Long existingId;
    private Long nonExistingId;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;
    private Long dependentId;

    @BeforeEach
    void setup() {
        existingId = 1L;
        nonExistingId = 9999999L;
        dependentId = 4L;
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of());

        when(service.findAll(ArgumentMatchers.any())).thenReturn(page);
        when(service.findById(existingId)).thenReturn(productDTO);
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(service.update(eq(existingId), any())).thenReturn(productDTO);
        when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        when(service.insert(any())).thenReturn(productDTO);

        doNothing().when(service).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        doThrow(DatabaseException.class).when(service).delete(dependentId);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        ResultActions result = mockMvc.
                perform(get("/products")
                        .accept("application/json"));

        result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        ResultActions result = mockMvc.
                perform(get("/products/" + existingId)
                        .accept("application/json"));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").value(productDTO.getName()));
        result.andExpect(jsonPath("$.description").value(productDTO.getDescription()));
        result.andExpect(jsonPath("$.price").value(productDTO.getPrice()));
        result.andExpect(jsonPath("$.imgUrl").value(productDTO.getImgUrl()));
        result.andExpect(jsonPath("$.categories").isArray());
        result.andExpect(jsonPath("$.categories[0].id").value(productDTO.getCategories().getFirst().getId()));
        result.andExpect(jsonPath("$.categories[0].name").value(productDTO.getCategories().getFirst().getName()));
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result = mockMvc.
                perform(get("/products/" + nonExistingId)
                        .accept("application/json"));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductWhenIdExists() throws Exception {

        String json = objectMapper.writeValueAsString(productDTO);
        ResultActions result = mockMvc.
                perform(put("/products/{id}", existingId)
                        .content(json)
                        .contentType("application/json")
                        .accept("application/json"));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").value(productDTO.getName()));
        result.andExpect(jsonPath("$.description").value(productDTO.getDescription()));
        result.andExpect(jsonPath("$.price").value(productDTO.getPrice()));
        result.andExpect(jsonPath("$.imgUrl").value(productDTO.getImgUrl()));
        result.andExpect(jsonPath("$.categories").isArray());
        result.andExpect(jsonPath("$.categories[0].id").value(productDTO.getCategories().getFirst().getId()));
        result.andExpect(jsonPath("$.categories[0].name").value(productDTO.getCategories().getFirst().getName()));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        String json = objectMapper.writeValueAsString(productDTO);
        ResultActions result = mockMvc.
                perform(put("/products/" + nonExistingId)
                        .content(json)
                        .contentType("application/json")
                        .accept("application/json"));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnProductDTOCreated() throws Exception {
        String json = objectMapper.writeValueAsString(productDTO);
        ResultActions result = mockMvc.
                perform(post("/products")
                        .content(json)
                        .contentType("application/json")
                        .accept("application/json"));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").value(productDTO.getName()));
        result.andExpect(jsonPath("$.description").value(productDTO.getDescription()));
        result.andExpect(jsonPath("$.price").value(productDTO.getPrice()));
        result.andExpect(jsonPath("$.imgUrl").value(productDTO.getImgUrl()));
        result.andExpect(jsonPath("$.categories").isArray());
        result.andExpect(jsonPath("$.categories[0].id").value(productDTO.getCategories().getFirst().getId()));
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        ResultActions result = mockMvc.
                perform(delete("/products/" + existingId)
                        .accept("application/json"));

        result.andExpect(status().isNoContent());
        verify(service, times(1)).delete(existingId);
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result = mockMvc.
                perform(delete("/products/" + nonExistingId)
                        .accept("application/json"));

        result.andExpect(status().isNotFound());
        verify(service, times(1)).delete(nonExistingId);
    }
}
