package com.agromarket.api.repository;

import com.agromarket.api.entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {
    Optional<Producer> findByDocument(String document);
}