package com.example.demo.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.example.demo.model.GameSale;

public interface GameSaleRepository extends JpaRepository<GameSale, Long> {
    // Method to find game sales within a date range
    @Query("SELECT gs FROM GameSale gs WHERE gs.dateOfSale BETWEEN :fromDate AND :toDate")
    Page<GameSale> findByDateOfSaleBetween(LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);

    // Method to find game sales with sale price greater than a specified amount
    @Query("SELECT gs FROM GameSale gs WHERE gs.salePrice > :price")
    Page<GameSale> findBySalePriceGreaterThan(BigDecimal price, Pageable pageable);

    // Method to find game sales with sale price less than a specified amount
    @Query("SELECT gs FROM GameSale gs WHERE gs.salePrice < :price")
    Page<GameSale> findBySalePriceLessThan(BigDecimal price, Pageable pageable);

    @Query("SELECT COUNT(gs) FROM GameSale gs WHERE gs.dateOfSale BETWEEN :fromDate AND :toDate")
    Long countGamesSoldByDateRange(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

    @Query("SELECT SUM(gs.salePrice) FROM GameSale gs WHERE gs.dateOfSale BETWEEN :fromDate AND :toDate")
    BigDecimal sumSalesByDateRange(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

    @Query("SELECT SUM(gs.salePrice) FROM GameSale gs WHERE gs.dateOfSale BETWEEN :fromDate AND :toDate AND gs.gameNo = :gameNo")
    BigDecimal sumSalesByDateRangeAndGameNo(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate, @Param("gameNo") Integer gameNo);
}

