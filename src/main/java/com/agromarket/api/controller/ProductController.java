
package com.agromarket.api.controller;

import com.agromarket.api.dto.ProductRequestDTO;
import com.agromarket.api.dto.ProductResponseDTO;
import com.agromarket.api.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDTO create(
        @Valid @RequestBody ProductRequestDTO requestDTO
    ) {
        return productService.create(requestDTO);
    }

    @GetMapping
    public List<ProductResponseDTO> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponseDTO findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO update(
        @PathVariable Long id,
        @Valid @RequestBody ProductRequestDTO requestDTO
    ) {
        return productService.update(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}
