CREATE TABLE game_sales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_no INT NOT NULL,
    game_name VARCHAR(20) NOT NULL,
    game_code VARCHAR(5) NOT NULL,
    type INT NOT NULL,
    cost_price DECIMAL(10, 2) NOT NULL,
    tax DECIMAL(10, 2) NOT NULL,
    sale_price DECIMAL(10, 2) NOT NULL,
    date_of_sale TIMESTAMP NOT NULL
);

CREATE TABLE csv_imports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    total_records INT DEFAULT 0,
    imported_records INT DEFAULT 0,
    status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED', 'FAILED') DEFAULT 'PENDING',
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP NULL
);

CREATE TABLE csv_import_errors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    import_id BIGINT NOT NULL,
    error_message TEXT NOT NULL,
    line_number INT NOT NULL,
    FOREIGN KEY (import_id) REFERENCES csv_imports(id) ON DELETE CASCADE
);

CREATE INDEX idx_date_of_sale ON game_sales(date_of_sale);
CREATE INDEX idx_sale_price ON game_sales(sale_price);
CREATE INDEX idx_game_no ON game_sales(game_no);
