
package com.agromarket.api.service;

import com.agromarket.api.dto.PriceAnalyticsResponseDTO;
import com.agromarket.api.dto.PriceHistoryResponseDTO;
import com.agromarket.api.dto.PriceVariationResponseDTO;
import com.agromarket.api.entity.MarketQuote;
import com.agromarket.api.repository.MarketQuoteRepository;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class MarketQuoteAnalyticsService {

        private final MarketQuoteRepository repository;

        public MarketQuoteAnalyticsService(
                        MarketQuoteRepository repository) {
                this.repository = repository;
        }

        // Análise de preços sem unidade explícita.
        public PriceAnalyticsResponseDTO analyzePrices(
                        String commodity,
                        LocalDate startDate,
                        LocalDate endDate) {

                return analyzePrices(
                                commodity,
                                startDate,
                                endDate,
                                null);
        }

        // Análise de preços com filtro por unidade.
        public PriceAnalyticsResponseDTO analyzePrices(
                        String commodity,
                        LocalDate startDate,
                        LocalDate endDate,
                        String unit) {

                validateParameters(commodity, startDate, endDate);

                List<MarketQuote> quotes = findQuotes(
                                commodity, startDate, endDate);

                String targetUnit = resolveTargetUnit(quotes, unit);

                List<MarketQuote> comparableQuotes = filterByUnit(
                                quotes, targetUnit, unit);

                BigDecimal minimumPrice = comparableQuotes.stream()
                                .map(MarketQuote::getPrice)
                                .min(BigDecimal::compareTo)
                                .orElseThrow();

                BigDecimal maximumPrice = comparableQuotes.stream()
                                .map(MarketQuote::getPrice)
                                .max(BigDecimal::compareTo)
                                .orElseThrow();

                BigDecimal totalPrice = comparableQuotes.stream()
                                .map(MarketQuote::getPrice)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal averagePrice = totalPrice.divide(
                                BigDecimal.valueOf(comparableQuotes.size()),
                                2,
                                RoundingMode.HALF_UP);

                return new PriceAnalyticsResponseDTO(
                                comparableQuotes.get(0).getCommodity(),
                                startDate,
                                endDate,
                                comparableQuotes.size(),
                                averagePrice,
                                minimumPrice,
                                maximumPrice);
        }

        // Histórico diário de preços.
        public PriceHistoryResponseDTO getPriceHistory(
                        String commodity,
                        LocalDate startDate,
                        LocalDate endDate,
                        String unit) {

                validateParameters(commodity, startDate, endDate);

                List<MarketQuote> quotes = findQuotes(
                                commodity, startDate, endDate);

                String targetUnit = resolveTargetUnit(quotes, unit);

                List<MarketQuote> comparableQuotes = filterByUnit(
                                quotes, targetUnit, unit);

                Map<LocalDate, List<MarketQuote>> groupedQuotes = comparableQuotes.stream()
                                .collect(Collectors.groupingBy(
                                                MarketQuote::getReferenceDate,
                                                TreeMap::new,
                                                Collectors.toList()));

                List<PriceHistoryResponseDTO.PriceHistoryPointDTO> history = groupedQuotes.entrySet()
                                .stream()
                                .map(entry -> {
                                        LocalDate date = entry.getKey();
                                        List<MarketQuote> dailyQuotes = entry.getValue();

                                        BigDecimal total = dailyQuotes.stream()
                                                        .map(MarketQuote::getPrice)
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                                        BigDecimal average = total.divide(
                                                        BigDecimal.valueOf(dailyQuotes.size()),
                                                        2,
                                                        RoundingMode.HALF_UP);

                                        BigDecimal minimum = dailyQuotes.stream()
                                                        .map(MarketQuote::getPrice)
                                                        .min(BigDecimal::compareTo)
                                                        .orElseThrow();

                                        BigDecimal maximum = dailyQuotes.stream()
                                                        .map(MarketQuote::getPrice)
                                                        .max(BigDecimal::compareTo)
                                                        .orElseThrow();

                                        return new PriceHistoryResponseDTO.PriceHistoryPointDTO(
                                                        date,
                                                        dailyQuotes.size(),
                                                        average,
                                                        minimum,
                                                        maximum);
                                })
                                .toList();

                return new PriceHistoryResponseDTO(
                                comparableQuotes.get(0).getCommodity(),
                                targetUnit,
                                startDate,
                                endDate,
                                history);
        }

        private void validateParameters(
                        String commodity,
                        LocalDate startDate,
                        LocalDate endDate) {

                if (commodity == null || commodity.isBlank()) {
                        throw new IllegalArgumentException(
                                        "A commodity é obrigatória.");
                }

                if (startDate == null || endDate == null) {
                        throw new IllegalArgumentException(
                                        "As datas inicial e final são obrigatórias.");
                }

                if (startDate.isAfter(endDate)) {
                        throw new IllegalArgumentException(
                                        "A data inicial não pode ser posterior à data final.");
                }
        }

        private List<MarketQuote> findQuotes(
                        String commodity,
                        LocalDate startDate,
                        LocalDate endDate) {

                List<MarketQuote> quotes = repository
                                .findByCommodityIgnoreCaseAndReferenceDateBetweenOrderByReferenceDateAsc(
                                                commodity.trim(),
                                                startDate,
                                                endDate);

                if (quotes.isEmpty()) {
                        throw new IllegalArgumentException(
                                        "Nenhuma cotação encontrada para essa commodity no período informado.");
                }

                return quotes;
        }

        private String resolveTargetUnit(
                        List<MarketQuote> quotes,
                        String unit) {

                if (unit != null && !unit.isBlank()) {
                        return normalizeUnit(unit);
                }

                return normalizeUnit(quotes.get(0).getUnit());
        }

        private List<MarketQuote> filterByUnit(
                        List<MarketQuote> quotes,
                        String targetUnit,
                        String requestedUnit) {

                List<MarketQuote> comparableQuotes = quotes.stream()
                                .filter(quote -> normalizeUnit(quote.getUnit()).equals(targetUnit))
                                .toList();

                if (comparableQuotes.isEmpty()) {
                        throw new IllegalArgumentException(
                                        "Nenhuma cotação encontrada para a unidade informada: "
                                                        + requestedUnit);
                }

                return comparableQuotes;
        }

        private static String normalizeUnit(String unit) {
                if (unit == null || unit.isBlank()) {
                        return "";
                }

                String normalized = removeAccents(unit)
                                .trim()
                                .toLowerCase(Locale.ROOT)
                                .replaceAll("\\s+", " ");

                return switch (normalized) {
                        case "saca",
                                        "saca 60 kg",
                                        "saca de 60 kg",
                                        "r$/saca",
                                        "r$ / saca",
                                        "r$/saca de 60 kg",
                                        "r$ / saca de 60 kg" ->
                                "r$/saca de 60 kg";

                        case "tonelada",
                                        "r$/tonelada",
                                        "r$ / tonelada",
                                        "r$/t",
                                        "r$ / t" ->
                                "r$/tonelada";

                        default -> normalized;
                };
        }

        private static String removeAccents(String value) {
                return Normalizer.normalize(value, Normalizer.Form.NFD)
                                .replaceAll("\\p{M}", "");
        }

        public PriceVariationResponseDTO getPriceVariation(
                        String commodity,
                        LocalDate startDate,
                        LocalDate endDate,
                        String unit) {

                if (commodity == null || commodity.isBlank()) {
                        throw new IllegalArgumentException(
                                        "A commodity é obrigatória.");
                }

                if (startDate == null || endDate == null) {
                        throw new IllegalArgumentException(
                                        "As datas inicial e final são obrigatórias.");
                }

                if (startDate.isAfter(endDate)) {
                        throw new IllegalArgumentException(
                                        "A data inicial não pode ser posterior à data final.");
                }

                var quotes = repository
                                .findByCommodityIgnoreCaseAndReferenceDateBetweenOrderByReferenceDateAsc(
                                                commodity.trim(), startDate, endDate)
                                .stream()
                                .filter(q -> unit == null || unit.isBlank()
                                                || normalizeUnit(q.getUnit())
                                                                .equals(normalizeUnit(unit)))
                                .toList();

                if (quotes.isEmpty()) {
                        throw new IllegalArgumentException(
                                        "Nenhuma cotação encontrada para os filtros informados.");
                }

                var dailyPrices = quotes.stream()
                                .collect(java.util.stream.Collectors.groupingBy(
                                                MarketQuote::getReferenceDate,
                                                java.util.TreeMap::new,
                                                java.util.stream.Collectors.averagingDouble(
                                                                q -> q.getPrice().doubleValue())));

                var firstEntry = dailyPrices.firstEntry();
                var lastEntry = dailyPrices.lastEntry();

                BigDecimal initialPrice = BigDecimal.valueOf(
                                firstEntry.getValue()).setScale(2,
                                                java.math.RoundingMode.HALF_UP);

                BigDecimal finalPrice = BigDecimal.valueOf(
                                lastEntry.getValue()).setScale(2,
                                                java.math.RoundingMode.HALF_UP);

                if (initialPrice.signum() == 0) {
                        throw new IllegalArgumentException(
                                        "Não é possível calcular a variação percentual com preço inicial zero.");
                }

                BigDecimal absoluteVariation = finalPrice
                                .subtract(initialPrice)
                                .setScale(2, java.math.RoundingMode.HALF_UP);

                BigDecimal percentageVariation = absoluteVariation
                                .multiply(BigDecimal.valueOf(100))
                                .divide(initialPrice, 2,
                                                java.math.RoundingMode.HALF_UP);

                String trend;

                if (absoluteVariation.signum() > 0) {
                        trend = "ALTA";
                } else if (absoluteVariation.signum() < 0) {
                        trend = "QUEDA";
                } else {
                        trend = "ESTAVEL";
                }

                String normalizedUnit = unit == null || unit.isBlank()
                                ? quotes.get(0).getUnit()
                                : normalizeUnit(unit);

                return new PriceVariationResponseDTO(
                                commodity.trim(),
                                normalizedUnit,
                                startDate,
                                endDate,
                                firstEntry.getKey(),
                                lastEntry.getKey(),
                                initialPrice,
                                finalPrice,
                                absoluteVariation,
                                percentageVariation,
                                trend);
        }

}
