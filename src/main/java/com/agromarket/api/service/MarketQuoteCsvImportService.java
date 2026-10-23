
package com.agromarket.api.service;

import com.agromarket.api.entity.MarketQuote;
import com.agromarket.api.repository.MarketQuoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class MarketQuoteCsvImportService {

    private final MarketQuoteRepository repository;

    public MarketQuoteCsvImportService(MarketQuoteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ImportResult importCsv(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Envie um arquivo CSV não vazio.");
        }

        List<MarketQuote> quotes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        file.getInputStream(),
                        StandardCharsets.UTF_8))) {

            String headerLine = reader.readLine();

            if (headerLine == null || headerLine.isBlank()) {
                throw new IllegalArgumentException(
                        "O CSV está vazio ou não possui cabeçalho.");
            }

            char delimiter = detectDelimiter(headerLine);

            List<String> headers = parseLine(headerLine, delimiter)
                    .stream()
                    .map(MarketQuoteCsvImportService::normalizeHeader)
                    .toList();

            Map<String, Integer> columns = indexHeaders(headers);

            requireColumn(columns, "commodity",
                    "commodity", "produto", "cultura");

            requireColumn(columns, "region",
                    "region", "regiao", "praca", "municipio");

            requireColumn(columns, "price",
                    "price", "preco", "valor");

            requireColumn(columns, "unit",
                    "unit", "unidade");

            requireColumn(columns, "referenceDate",
                    "referencedate", "data",
                    "data referencia", "data de referencia");

            requireColumn(columns, "source",
                    "source", "fonte");

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                try {
                    List<String> values = parseLine(line, delimiter);

                    MarketQuote quote = new MarketQuote();

                    quote.setCommodity(
                            value(values, columns, "commodity").trim());

                    quote.setRegion(
                            value(values, columns, "region").trim());

                    String state = optionalValue(
                            values, columns, "state");

                    quote.setState(
                            state.isBlank()
                                    ? null
                                    : state.trim().toUpperCase(Locale.ROOT));

                    quote.setPrice(
                            parsePrice(value(values, columns, "price")));

                    quote.setUnit(
                            normalizeUnit(value(values, columns, "unit")));

                    quote.setReferenceDate(
                            parseDate(value(
                                    values, columns, "referenceDate").trim()));

                    quote.setSource(
                            value(values, columns, "source").trim());

                    quote.setDataType("IMPORTED");

                    if (quote.getCommodity().isBlank()
                            || quote.getRegion().isBlank()
                            || quote.getUnit().isBlank()
                            || quote.getSource().isBlank()) {
                        throw new IllegalArgumentException(
                                "há campo obrigatório vazio");
                    }

                    quotes.add(quote);

                } catch (RuntimeException ex) {
                    throw new IllegalArgumentException(
                            "Erro na linha " + lineNumber + ": "
                                    + ex.getMessage(),
                            ex);
                }
            }
        }

        if (quotes.isEmpty()) {
            throw new IllegalArgumentException(
                    "Nenhuma cotação válida foi encontrada no CSV.");
        }

        repository.saveAll(quotes);

        return new ImportResult(
                quotes.size(),
                "Importação concluída. Confira a fonte e as datas das cotações.");
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
                "R$/saca de 60 kg";

            default -> unit.trim();
        };
    }

    private static String removeAccents(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }

    private static char detectDelimiter(String line) {
        int semicolons = countOutsideQuotes(line, ';');
        int commas = countOutsideQuotes(line, ',');

        return semicolons >= commas ? ';' : ',';
    }

    private static int countOutsideQuotes(String line, char target) {
        boolean quoted = false;
        int count = 0;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (quoted && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (c == target && !quoted) {
                count++;
            }
        }

        return count;
    }

    private static List<String> parseLine(
            String line,
            char delimiter) {

        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (quoted && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (c == delimiter && !quoted) {
                values.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        if (quoted) {
            throw new IllegalArgumentException(
                    "campo com aspas não foi fechado");
        }

        values.add(current.toString().trim());

        return values;
    }

    private static Map<String, Integer> indexHeaders(
            List<String> headers) {

        Map<String, Integer> result = new HashMap<>();

        for (int i = 0; i < headers.size(); i++) {
            result.put(headers.get(i), i);
        }

        return result;
    }

    private static void requireColumn(
            Map<String, Integer> columns,
            String key,
            String... aliases) {

        for (String alias : aliases) {
            Integer index = columns.get(normalizeHeader(alias));

            if (index != null) {
                columns.put(key, index);
                return;
            }
        }

        throw new IllegalArgumentException(
                "Coluna obrigatória ausente: " + key
                        + ". Cabeçalhos aceitos: "
                        + String.join(", ", aliases));
    }

    private static String normalizeHeader(String header) {
        String normalized = header == null
                ? ""
                : header.replace("\uFEFF", "")
                        .trim()
                        .toLowerCase(Locale.ROOT);

        normalized = removeAccents(normalized)
                .replace("_", " ")
                .replace("-", " ");

        return normalized.replaceAll("\\s+", " ");
    }

    private static String value(
            List<String> values,
            Map<String, Integer> columns,
            String key) {

        Integer index = columns.get(key);

        if (index == null || index >= values.size()) {
            throw new IllegalArgumentException(
                    "valor ausente na coluna " + key);
        }

        return values.get(index);
    }

    private static String optionalValue(
            List<String> values,
            Map<String, Integer> columns,
            String key) {

        Integer index = columns.get(key);

        return index == null || index >= values.size()
                ? ""
                : values.get(index);
    }

    private static BigDecimal parsePrice(String raw) {
        String value = raw.trim()
                .replace("R$", "")
                .replace(" ", "");

        if (value.contains(",") && value.contains(".")) {
            if (value.lastIndexOf(',') > value.lastIndexOf('.')) {
                value = value.replace(".", "")
                        .replace(",", ".");
            } else {
                value = value.replace(",", "");
            }
        } else if (value.contains(",")) {
            value = value.replace(",", ".");
        }

        BigDecimal price = new BigDecimal(value);

        if (price.signum() < 0) {
            throw new IllegalArgumentException(
                    "o preço não pode ser negativo");
        }

        return price;
    }

    private static LocalDate parseDate(String raw) {
        try {
            if (raw.matches("\\d{2}/\\d{2}/\\d{4}")) {
                String[] parts = raw.split("/");

                return LocalDate.of(
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[0]));
            }

            return LocalDate.parse(raw);

        } catch (DateTimeException | NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "data inválida: " + raw
                            + ". Use AAAA-MM-DD ou DD/MM/AAAA.");
        }
    }

    public record ImportResult(int imported, String message) {
    }
}
