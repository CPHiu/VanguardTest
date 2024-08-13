package com.example.demo.service;

import com.example.demo.model.CsvImport;
import com.example.demo.model.CsvImportError;
import com.example.demo.model.GameSale;
import com.example.demo.repository.CsvImportErrorRepository;
import com.example.demo.repository.CsvImportRepository;
import com.example.demo.repository.GameSaleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.*;

@Service
@EnableSpringDataWebSupport(
        pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO
)
public class GameSaleService {

    private final int THREAD_COUNT = 8; // Adjust based on available CPU cores
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

    @Autowired
    private GameSaleRepository gameSaleRepository;

    @Autowired
    private CsvImportRepository csvImportRepository;

    @Autowired
    private CsvImportErrorRepository csvImportErrorRepository;

    @Transactional
    public void importCSV(MultipartFile file) throws Exception {
        int batchSize = 5000;
        CsvImport csvImport = new CsvImport();
        csvImport.setFileName(file.getOriginalFilename());
        csvImport.setStatus("IN_PROGRESS");
        csvImport = csvImportRepository.saveAndFlush(csvImport);

        List<Future<Integer>> futures = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            reader.readLine(); // Skip header

            List<String> batchLines = new ArrayList<>(batchSize);
            while ((line = reader.readLine()) != null) {
                batchLines.add(line);

                if (batchLines.size() == batchSize) {
                    List<String> linesToProcess = new ArrayList<>(batchLines);
                    CsvImport finalCsvImport1 = csvImport;
                    futures.add(executorService.submit(() -> processBatch(linesToProcess, finalCsvImport1)));
                    batchLines.clear();
                }
            }

            // Process remaining lines
            if (!batchLines.isEmpty()) {
                List<String> linesToProcess = new ArrayList<>(batchLines);
                CsvImport finalCsvImport = csvImport;
                futures.add(executorService.submit(() -> processBatch(linesToProcess, finalCsvImport)));
            }

            for (Future<Integer> future : futures) {
                csvImport.setImportedRecords(csvImport.getImportedRecords() + future.get());
            }

            csvImport.setTotalRecords(csvImport.getImportedRecords());
            csvImport.setStatus("COMPLETED");
        } catch (Exception e) {
            csvImport.setStatus("FAILED");
        } finally {
            csvImport.setEndTime(LocalDateTime.now());
            csvImportRepository.save(csvImport);
        }
    }

    private int processBatch(List<String> lines, CsvImport csvImport) {
        List<GameSale> gameSales = new ArrayList<>(lines.size());
        int importedCount = 0;

        for (String line : lines) {
            String[] fields = line.split(",");
            try {
                GameSale gameSale = new GameSale();
                gameSale.setGameNo(Integer.parseInt(fields[1].trim()));
                gameSale.setGameName(fields[2].trim());
                gameSale.setGameCode(fields[3].trim());
                gameSale.setType(Integer.parseInt(fields[4].trim()));
                gameSale.setCostPrice(new BigDecimal(fields[5].trim()));
                gameSale.setTax(new BigDecimal(fields[6].trim()));
                gameSale.setSalePrice(new BigDecimal(fields[7].trim()));
                gameSale.setDateOfSale(LocalDateTime.parse(fields[8].trim(), DateTimeFormatter.ISO_DATE_TIME));

                gameSales.add(gameSale);
                importedCount++;
            } catch (Exception e) {
                CsvImportError error = new CsvImportError();
                error.setCsvImport(csvImport);
                error.setErrorMessage(e.getMessage());
                error.setLineNumber(importedCount + 1);
                csvImportErrorRepository.save(error);
            }
        }

        if (!gameSales.isEmpty()) {
            gameSaleRepository.saveAll(gameSales);
        }

        return importedCount;
    }

    @Async("taskExecutor")
    public CompletableFuture<Page<GameSale>> getAllGameSales(Pageable pageable) {
        Page<GameSale> result = gameSaleRepository.findAll(pageable);
        return CompletableFuture.completedFuture(result);
    }

    @Async("taskExecutor")
    public CompletableFuture<Page<GameSale>> getGameSalesByDateRange(LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable) {
        Page<GameSale> result = gameSaleRepository.findByDateOfSaleBetween(fromDate, toDate, pageable);
        return CompletableFuture.completedFuture(result);
    }

    @Async("taskExecutor")
    public CompletableFuture<Page<GameSale>> getGameSalesByPrice(BigDecimal price, String condition, Pageable pageable) {
        Page<GameSale> result;
        if ("more".equalsIgnoreCase(condition)) {
            result = gameSaleRepository.findBySalePriceGreaterThan(price, pageable);
        } else if ("less".equalsIgnoreCase(condition)) {
            result = gameSaleRepository.findBySalePriceLessThan(price, pageable);
        } else {
            throw new IllegalArgumentException("Invalid condition: " + condition);
        }
        return CompletableFuture.completedFuture(result);
    }

    @Async("taskExecutor")
    public CompletableFuture<Long> getTotalGamesSold(LocalDateTime fromDate, LocalDateTime toDate) {
        return CompletableFuture.supplyAsync(() -> gameSaleRepository.countGamesSoldByDateRange(fromDate, toDate));
    }

    @Async("taskExecutor")
    public CompletableFuture<BigDecimal> getTotalSales(LocalDateTime fromDate, LocalDateTime toDate, Integer gameNo) {
        return CompletableFuture.supplyAsync(() -> {
            if (gameNo != null) {
                return gameSaleRepository.sumSalesByDateRangeAndGameNo(fromDate, toDate, gameNo);
            } else {
                return gameSaleRepository.sumSalesByDateRange(fromDate, toDate);
            }
        });
    }
}
