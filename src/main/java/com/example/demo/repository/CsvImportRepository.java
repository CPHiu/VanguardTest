package com.example.demo.repository;

import com.example.demo.model.CsvImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for CsvImportProgress entity.
 */
@Repository
public interface CsvImportRepository extends JpaRepository<CsvImport, Long> {
    // You can add custom query methods if needed, but JpaRepository provides basic CRUD operations
}
