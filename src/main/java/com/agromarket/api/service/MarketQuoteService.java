
package com.agromarket.api.service;

import com.agromarket.api.dto.MarketQuoteRequestDTO;
import com.agromarket.api.dto.MarketQuoteResponseDTO;
import com.agromarket.api.entity.MarketQuote;
import com.agromarket.api.repository.MarketQuoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MarketQuoteService {

    private final MarketQuoteRepository repository;

    public MarketQuoteService(MarketQuoteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public MarketQuoteResponseDTO create(MarketQuoteRequestDTO request) {
        MarketQuote quote = new MarketQuote();

        quote.setCommodity(request.commodity().trim());
        quote.setRegion(request.region().trim());
        quote.setState(request.state() == null
                ? null : request.state().trim());
        quote.setPrice(request.price());
        quote.setUnit(request.unit().trim());
        quote.setReferenceDate(request.referenceDate());
        quote.setSource(request.source().trim());
        quote.setDataType("MANUAL");

        return toResponse(repository.save(quote));
    }

    @Transactional(readOnly = true)
    public List<MarketQuoteResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MarketQuoteResponseDTO findById(Long id) {
        MarketQuote quote = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cotação não encontrada: " + id));

        return toResponse(quote);
    }

    private MarketQuoteResponseDTO toResponse(MarketQuote quote) {
        return new MarketQuoteResponseDTO(
                quote.getId(),
                quote.getCommodity(),
                quote.getRegion(),
                quote.getState(),
                quote.getPrice(),
                quote.getUnit(),
                quote.getReferenceDate(),
                quote.getSource(),
                quote.getDataType(),
                quote.getCreatedAt()
        );
    }
}
