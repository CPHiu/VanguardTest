# Game Sales Management System

This is a Spring Boot application for managing game sales, including importing large CSV files, querying sales data, and tracking import progress. The application uses MySQL as its database.

## Features

- Import CSV files with game sales data.
- Query game sales with filtering and pagination.
- Track import progress and errors.
- Calculate total sales and game counts over specific periods.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) 17 or higher.
- Apache Maven 3.6.0 or higher.
- MySQL Server 5.7 or higher.
- Git for version control.

## Setup Instructions

### Clone the Repository

```bash
git clone https://github.com/CPHiu/VanguardTest.git
cd game-sales-management
```

### Configure the Database

1. Create a MySQL database for the application:

   ```sql
   CREATE DATABASE game_sales_db;
   ```

2. Update the database credentials in `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/game_sales_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

### Build the Application

Use Maven to build the project:

```bash
./mvnw clean install
```

This command compiles the project, runs tests, and packages the application into a JAR file located in the `target` directory.

### Run the Application

After building the application, you can run it using the following command:

```bash
./mvnw spring-boot:run
```

Or, if you prefer to run the packaged JAR file:

```bash
java -jar target/game-sales-management-0.0.1-SNAPSHOT.jar
```

### Access the Application

Once the application is running, you can access it via the following endpoints:

- **Import CSV File:** `POST /api/import`  
  Upload a CSV file containing game sales data. Use a tool like Postman to test this endpoint.

- **Get Game Sales:** `GET /api/getGameSales`  
  Query game sales with optional parameters for pagination and filters (e.g., `fromDate`, `toDate`, `price`, `condition`).

- **Get Total Sales:** `GET /api/getTotalSales`  
  Retrieve total sales and game counts over a specified period with optional `gameNo` filtering.

## Performance Considerations

To optimize performance and ensure fast data imports, consider the following:

- Use a batch size of 5000 for CSV imports.
- Ensure indexes are created on `date_of_sale` and `sale_price` columns in the database.
- Configure database connection pooling settings in `application.properties` for optimal performance.

## Contributing

Contributions are welcome! Please follow these steps to contribute:

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature-name`.
3. Commit your changes: `git commit -m 'Add feature'`.
4. Push to the branch: `git push origin feature-name`.
5. Open a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For questions or support, please contact [jauzeonping@yahoo.com](mailto:jauzeonping@yahoo.com).

