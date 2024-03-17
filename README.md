
# Qred Payment Service

The Qred Payment Service is a Spring Boot application designed to manage payments, contracts, and client information. This application leverages the reactive programming model with R2DBC for reactive database access and WebFlux for handling web requests reactively.

## Prerequisites

Before you begin, ensure you have met the following requirements:
- Java JDK 11 or later installed
- Docker installed (for running the MySQL database)
- Maven installed (for building the project)

## Installation

1. **Start the MySQL Database with Docker Compose**

   Navigate to the project root directory and run:

   ```
   docker-compose up -d
   ```

   This command starts a MySQL instance preconfigured with the necessary database and tables.

2. **Build the Application**

   In the project root directory, run:

   ```
   mvn clean install
   ```

## Running the Application

After building the project, you can run it with:

```
mvn spring-boot:run
```

The application will be available at `http://localhost:8082`.

## API Documentation

The API documentation is available via Swagger UI at `http://localhost:8082/swagger-ui.html`. This interface allows you to test and interact with the API endpoints directly from your browser.

## Testing

To run the tests included in the project, use the following Maven command:

```
mvn test
```

This command runs all unit and integration tests defined in the project.

## Usage

Here are some example requests you can make to the application:

- **Creating a Payment**

  ```
  POST /payments
  Content-Type: application/json

  {
    "paymentDate": "2024-03-01",
    "amount": 1200.00,
    "paymentType": "INCOMING",
    "contractNumber": "A-001"
  }
  ```

- **Fetching Payments for a Contract**

  ```
  GET /payments/contract/{contractNumber}
  ```

Replace `{contractNumber}` with the actual contract number.

For more details on the available endpoints and their usage, refer to the API documentation through Swagger UI.
