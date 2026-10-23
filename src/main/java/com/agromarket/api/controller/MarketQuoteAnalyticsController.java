
package com.agromarket.api.controller;

import com.agromarket.api.dto.PriceAnalyticsResponseDTO;
import com.agromarket.api.dto.PriceHistoryResponseDTO;
import com.agromarket.api.dto.PriceVariationResponseDTO;
import com.agromarket.api.service.MarketQuoteAnalyticsService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/analytics")
public class MarketQuoteAnalyticsController {

    private final MarketQuoteAnalyticsService service;

    public MarketQuoteAnalyticsController(
            MarketQuoteAnalyticsService service) {
        this.service = service;
    }

    @GetMapping("/prices")
    public PriceAnalyticsResponseDTO analyzePrices(
            @RequestParam String commodity,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,
            @RequestParam(required = false) String unit) {

        return service.analyzePrices(
                commodity,
                startDate,
                endDate,
                unit);
    }

    @GetMapping("/prices/history")
    public PriceHistoryResponseDTO getPriceHistory(
            @RequestParam String commodity,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,
            @RequestParam(required = false) String unit) {

        return service.getPriceHistory(
                commodity,
                startDate,
                endDate,
                unit);
    }

    @GetMapping("/prices/variation")
    public PriceVariationResponseDTO getPriceVariation(
            @RequestParam String commodity,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,
            @RequestParam(required = false) String unit) {

        return service.getPriceVariation(
                commodity,
                startDate,
                endDate,
                unit);
    }
}
