package com.agromarket.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "producers")
public class Producer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    private String name;

    @NotBlank(message = "O documento (CPF/CNPJ) é obrigatório")
    @Size(max = 20, message = "O documento deve ter no máximo 20 caracteres")
    @Column(unique = true)
    private String document;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O formato do e-mail é inválido")
    @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres")
    private String email;

    @NotBlank(message = "O telefone é obrigatório")
    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
    private String phone;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}