
package com.agromarket.api.controller;

import com.agromarket.api.dto.ProducerRequestDTO;
import com.agromarket.api.dto.ProducerResponseDTO;
import com.agromarket.api.service.ProducerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/producers")
public class ProducerController {

    private final ProducerService producerService;

    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping
    public ResponseEntity<ProducerResponseDTO> create(
            @Valid @RequestBody ProducerRequestDTO requestDTO) {

        ProducerResponseDTO response = producerService.create(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProducerResponseDTO>> findAll() {
        List<ProducerResponseDTO> producers = producerService.findAll();

        return ResponseEntity.ok(producers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProducerResponseDTO> findById(
            @PathVariable Long id) {

        ProducerResponseDTO producer = producerService.findById(id);

        return ResponseEntity.ok(producer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProducerResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProducerRequestDTO requestDTO) {

        ProducerResponseDTO response = producerService.update(id, requestDTO);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        producerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
