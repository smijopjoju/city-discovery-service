# City Discovery Service

A high-performance microservice designed to discover nearby urban hubs based on geographic coordinates. This service provides a simple REST API to find cities within a proximity search, ideal for data agent tasking and location-based services.

## Tech Stack

- **Language:** Java 25
- **Framework:** Spring Boot 4.0.3
- **Database:** H2 (In-memory)
- **Persistence:** Spring Data JPA
- **API Documentation:** SpringDoc OpenAPI (Swagger UI)
- **Build Tool:** Maven

## Getting Started

### Prerequisites

- **Java Development Kit (JDK) 25** or higher.
- **Maven** (included via `./mvnw` wrapper).

### Compilation

To compile the project and download dependencies, run:

```bash
./mvnw clean compile
```

### Running Tests

To execute the unit and integration test suite:

```bash
./mvnw test
```

### Running the Application

Start the service locally using the Spring Boot Maven plugin:

```bash
./mvnw spring-boot:run
```

The application will be available at `http://localhost:8080`.

## API Documentation

Once the application is running, you can explore the interactive API documentation at:

- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Main Endpoint

**`GET /cds/v1/cities/nearby`**

Discover the closest urban hubs based on a center point.

#### Query Parameters

| Parameter | Type | Required | Default | Description |
| :--- | :--- | :--- | :--- | :--- |
| `lat` | Double | Yes | - | Latitude of the center point (Range: -90 to 90). |
| `lng` | Double | Yes | - | Longitude of the center point (Range: -180 to 180). |
| `limit` | Integer | No | 10 | Number of cities to return (Max: 50). |

#### Example Request

```bash
curl "http://localhost:8080/cds/v1/cities/nearby?lat=40.7128&lng=-74.0060&limit=5"
```

## Developer Tools

### H2 Console

Inspect the in-memory database at: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

**Connection Details:**
- **JDBC URL:** `jdbc:h2:mem:citydb`
- **User Name:** `sa`
- **Password:** `password`

### Data Initialization

On startup, the service automatically initializes its database from the `src/main/resources/uscities.csv` file. This ensures the service is ready for discovery requests immediately upon deployment.

## Project Structure

- `src/main/java`: Core application logic, including controllers, services, and the discovery pipeline.
- `src/main/resources`: Configuration (`application.yaml`), schema definitions, and seed data.
- `src/test/java`: Comprehensive test suite including unit, service-level, and integration tests.
