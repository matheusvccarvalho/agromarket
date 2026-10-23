
package com.agromarket.api.controller;

import com.agromarket.api.dto.MarketQuoteRequestDTO;
import com.agromarket.api.dto.MarketQuoteResponseDTO;
import com.agromarket.api.service.MarketQuoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quotes")
public class MarketQuoteController {

    private final MarketQuoteService service;

    public MarketQuoteController(MarketQuoteService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MarketQuoteResponseDTO create(
            @Valid @RequestBody MarketQuoteRequestDTO request) {
        return service.create(request);
    }

    @GetMapping
    public List<MarketQuoteResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public MarketQuoteResponseDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }
}
