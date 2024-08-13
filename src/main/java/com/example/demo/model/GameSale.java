package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

@Entity
@Table(name = "game_sales")
public class GameSale implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_no", nullable = false)
    private Integer gameNo;

    @Column(name = "game_name", length = 20, nullable = false)
    private String gameName;

    @Column(name = "game_code", length = 5, nullable = false)
    private String gameCode;

    @Column(name = "type", nullable = false)
    private Integer type;

    @Column(name = "cost_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal costPrice;

    @Column(name = "tax", precision = 5, scale = 2, nullable = false)
    private BigDecimal tax;

    @Column(name = "sale_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal salePrice;

    @Column(name = "date_of_sale", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime dateOfSale;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGameNo() {
        return gameNo;
    }

    public void setGameNo(Integer gameNo) {
        this.gameNo = gameNo;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public LocalDateTime getDateOfSale() {
        return dateOfSale;
    }

    public void setDateOfSale(LocalDateTime dateOfSale) {
        this.dateOfSale = dateOfSale;
    }
}
