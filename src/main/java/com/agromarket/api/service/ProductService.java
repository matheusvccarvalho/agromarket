
package com.agromarket.api.service;

import com.agromarket.api.dto.ProductRequestDTO;
import com.agromarket.api.dto.ProductResponseDTO;
import com.agromarket.api.entity.Product;
import com.agromarket.api.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponseDTO create(ProductRequestDTO requestDTO) {
        Product product = new Product();
        updateProductFields(product, requestDTO);

        Product savedProduct = productRepository.save(product);
        return ProductResponseDTO.fromEntity(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll()
            .stream()
            .map(ProductResponseDTO::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Long id) {
        Product product = getProductOrThrow(id);
        return ProductResponseDTO.fromEntity(product);
    }

    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO requestDTO) {
        Product product = getProductOrThrow(id);
        updateProductFields(product, requestDTO);

        Product updatedProduct = productRepository.save(product);
        return ProductResponseDTO.fromEntity(updatedProduct);
    }

    @Transactional
    public void delete(Long id) {
        Product product = getProductOrThrow(id);
        productRepository.delete(product);
    }

    private Product getProductOrThrow(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() ->
                new NoSuchElementException(
                    "Produto não encontrado com o ID: " + id
                )
            );
    }

    private void updateProductFields(
        Product product,
        ProductRequestDTO requestDTO
    ) {
        product.setName(requestDTO.name());
        product.setCategory(requestDTO.category());
        product.setDescription(requestDTO.description());
        product.setUnit(requestDTO.unit());
        product.setPrice(requestDTO.price());
        product.setStockQuantity(requestDTO.stockQuantity());
        product.setMinimumStock(requestDTO.minimumStock());
    }
}
