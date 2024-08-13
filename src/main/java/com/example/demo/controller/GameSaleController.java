package com.example.demo.controller;

import com.example.demo.model.GameSale;
import com.example.demo.service.GameSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class GameSaleController {

    @Autowired
    private GameSaleService gameSaleService;

    @PostMapping("/import")
    public ResponseEntity<String> importCSV(@RequestParam("File") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a CSV file.");
        }

        try {
            gameSaleService.importCSV(file);
            return ResponseEntity.status(HttpStatus.OK).body("CSV data imported successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the CSV file.");
        }
    }

    @GetMapping("/getGameSales")
    public ResponseEntity<Page<GameSale>> getGameSales(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size,
            @RequestParam(value = "fromDate", required = false) String fromDateStr,
            @RequestParam(value = "toDate", required = false) String toDateStr,
            @RequestParam(value = "price", required = false) BigDecimal price,
            @RequestParam(value = "condition", required = false) String condition) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("dateOfSale").descending());

        try {
            CompletableFuture<Page<GameSale>> futureResult;
            if (fromDateStr != null && toDateStr != null) {
                LocalDateTime fromDate = LocalDate.parse(fromDateStr, DateTimeFormatter.ISO_DATE).atStartOfDay();
                LocalDateTime toDate = LocalDate.parse(toDateStr, DateTimeFormatter.ISO_DATE).atTime(LocalTime.MAX);
                futureResult = gameSaleService.getGameSalesByDateRange(fromDate, toDate, pageable);
            } else if (price != null && condition != null) {
                futureResult = gameSaleService.getGameSalesByPrice(price, condition, pageable);
            } else {
                futureResult = gameSaleService.getAllGameSales(pageable);
            }
            return ResponseEntity.ok(futureResult.get());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/getTotalSales")
    public ResponseEntity<Map<String, Object>> getTotalSales(
            @RequestParam("fromDate") String fromDateStr,
            @RequestParam("toDate") String toDateStr,
            @RequestParam(value = "gameNo", required = false) Integer gameNo) {

        LocalDateTime fromDate = LocalDate.parse(fromDateStr, DateTimeFormatter.ISO_DATE).atStartOfDay();
        LocalDateTime toDate = LocalDate.parse(toDateStr, DateTimeFormatter.ISO_DATE).atTime(LocalTime.MAX);

        Map<String, Object> result = new HashMap<>();

        try {
            CompletableFuture<Long> futureTotalGamesSold = gameSaleService.getTotalGamesSold(fromDate, toDate);
            CompletableFuture<BigDecimal> futureTotalSales = gameSaleService.getTotalSales(fromDate, toDate, gameNo);

            CompletableFuture.allOf(futureTotalGamesSold, futureTotalSales).join();

            result.put("totalGamesSold", futureTotalGamesSold.get());
            result.put("totalSales", futureTotalSales.get());

            return ResponseEntity.ok(result);
        } catch (ExecutionException | InterruptedException e) {
            result.put("error", "An error occurred while fetching the data.");
            return ResponseEntity.status(500).body(result);
        }
    }
}
