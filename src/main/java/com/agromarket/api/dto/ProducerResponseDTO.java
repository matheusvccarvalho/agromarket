package com.agromarket.api.dto;

import com.agromarket.api.entity.Producer;
import java.time.LocalDateTime;

public record ProducerResponseDTO(
    Long id,
    String name,
    String document,
    String email,
    String phone,
    LocalDateTime createdAt
) {
    public static ProducerResponseDTO fromEntity(Producer producer) {
        return new ProducerResponseDTO(
            producer.getId(),
            producer.getName(),
            producer.getDocument(),
            producer.getEmail(),
            producer.getPhone(),
            producer.getCreatedAt()
        );
    }
}