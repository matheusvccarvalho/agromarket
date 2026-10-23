
package com.agromarket.api.service;

import com.agromarket.api.dto.ProducerRequestDTO;
import com.agromarket.api.dto.ProducerResponseDTO;
import com.agromarket.api.entity.Producer;
import com.agromarket.api.repository.ProducerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProducerService {

    private final ProducerRepository producerRepository;

    public ProducerService(ProducerRepository producerRepository) {
        this.producerRepository = producerRepository;
    }

    @Transactional
    public ProducerResponseDTO create(ProducerRequestDTO requestDTO) {
        if (producerRepository.findByDocument(requestDTO.document()).isPresent()) {
            throw new IllegalArgumentException(
                    "Já existe um produtor cadastrado com este documento.");
        }

        Producer producer = new Producer();
        producer.setName(requestDTO.name());
        producer.setDocument(requestDTO.document());
        producer.setEmail(requestDTO.email());
        producer.setPhone(requestDTO.phone());

        Producer savedProducer = producerRepository.save(producer);

        return ProducerResponseDTO.fromEntity(savedProducer);
    }

    @Transactional(readOnly = true)
    public List<ProducerResponseDTO> findAll() {
        return producerRepository.findAll()
                .stream()
                .map(ProducerResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProducerResponseDTO findById(Long id) {
        Producer producer = producerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Produtor não encontrado com o ID: " + id));

        return ProducerResponseDTO.fromEntity(producer);
    }

    @Transactional
    public ProducerResponseDTO update(Long id, ProducerRequestDTO requestDTO) {
        Producer producer = producerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Produtor não encontrado com o ID: " + id));

        producerRepository.findByDocument(requestDTO.document())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new IllegalArgumentException(
                                "Já existe outro produtor cadastrado com este documento.");
                    }
                });

        producer.setName(requestDTO.name());
        producer.setDocument(requestDTO.document());
        producer.setEmail(requestDTO.email());
        producer.setPhone(requestDTO.phone());

        Producer updatedProducer = producerRepository.save(producer);

        return ProducerResponseDTO.fromEntity(updatedProducer);
    }

    @Transactional
    public void delete(Long id) {
        Producer producer = producerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Produtor não encontrado com o ID: " + id));

        producerRepository.delete(producer);
    }

}
