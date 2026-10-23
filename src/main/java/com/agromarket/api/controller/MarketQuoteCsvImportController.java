package com.agromarket.api.controller;

import com.agromarket.api.service.MarketQuoteCsvImportService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/quotes")
public class MarketQuoteCsvImportController {

    private final MarketQuoteCsvImportService importService;

    public MarketQuoteCsvImportController(MarketQuoteCsvImportService importService) {
        this.importService = importService;
    }

    @PostMapping(value = "/import-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> importCsv(@RequestParam("file") MultipartFile file) throws IOException {
        MarketQuoteCsvImportService.ImportResult result = importService.importCsv(file);
        return Map.of(
                "imported", result.imported(),
                "message", result.message()
        );
    }
}
