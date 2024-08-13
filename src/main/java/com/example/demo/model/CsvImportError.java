package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "csv_import_errors")
public class CsvImportError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "import_id", nullable = false)
    private CsvImport csvImport;

    @Column(name = "error_message", nullable = false)
    private String errorMessage;

    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CsvImport getCsvImport() {
        return csvImport;
    }

    public void setCsvImport(CsvImport csvImport) {
        this.csvImport = csvImport;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
