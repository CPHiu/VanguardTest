package com.example.demo.repository;

import com.example.demo.model.CsvImportError;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CsvImportErrorRepository extends JpaRepository<CsvImportError, Long> {
}