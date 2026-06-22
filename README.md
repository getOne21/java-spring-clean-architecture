<h1 align="center">🏗️ Java Spring Clean Architecture</h1>

<p align="center">
  A production-ready Spring Boot project template using <b>Java 21</b>, <b>Clean Architecture</b>, and <b>Hibernate/JPA</b> with a complete User CRUD example.
</p>

---

## 🏷️ Tech Stack

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![H2](https://img.shields.io/badge/H2-0000BB?style=for-the-badge&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

---

## 📐 Architecture Overview

This project follows **Clean Architecture** (Hexagonal / Ports & Adapters), ensuring clear separation of concerns and dependency inversion.

```
src/main/java/com/getone21/cleanarchitecture/
│
├── domain/                          # 🟢 Enterprise Business Rules (innermost layer)
│   ├── model/                       #     Domain entities (pure Java, no framework deps)
│   │   └── User.java
│   ├── port/
│   │   ├── in/                      #     Input ports (use cases / interfaces)
│   │   │   ├── CreateUserUseCase.java
│   │   │   ├── GetUserUseCase.java
│   │   │   ├── UpdateUserUseCase.java
│   │   │   └── DeleteUserUseCase.java
│   │   └── out/                     #     Output ports (repository interfaces)
│   │       └── UserRepositoryPort.java
│   └── exception/                   #     Domain-specific exceptions
│       ├── UserNotFoundException.java
│       └── UserAlreadyExistsException.java
│
├── application/                     # 🔵 Application Business Rules
│   └── service/
│       └── UserService.java         #     Implements use cases, orchestrates domain logic
│
├── infrastructure/                  # 🟠 Frameworks & Drivers (outermost layer)
│   ├── persistence/
│   │   ├── entity/
│   │   │   └── UserJpaEntity.java   #     JPA/Hibernate entity
│   │   ├── repository/
│   │   │   ├── JpaUserRepository.java       # Spring Data JPA repository
│   │   │   └── UserRepositoryAdapter.java   # Implements output port
│   │   └── mapper/
│   │       └── UserPersistenceMapper.java   # MapStruct: domain ↔ JPA entity
│   └── config/
│       └── OpenApiConfig.java       #     Swagger/OpenAPI configuration
│
└── presentation/                    # 🟣 Interface Adapters
    ├── controller/
    │   └── UserController.java      #     REST API endpoints
    ├── dto/
    │   ├── request/
    │   │   ├── CreateUserRequest.java   # Input DTOs with validation
    │   │   └── UpdateUserRequest.java
    │   └── response/
    │       ├── UserResponse.java        # Output DTOs
    │       └── ErrorResponse.java
    ├── mapper/
    │   └── UserApiMapper.java       #     MapStruct: DTO ↔ domain
    └── exception/
        └── GlobalExceptionHandler.java  # Centralized error handling
```

### Dependency Rule

Dependencies always point **inward**:

```
Presentation → Application → Domain ← Infrastructure
```

- **Domain** has zero external dependencies (no Spring, no JPA annotations)
- **Application** depends only on domain
- **Infrastructure** implements domain output ports
- **Presentation** calls domain input ports (use cases)

---

## 🚀 Getting Started

### Prerequisites

- **Java 21** or later
- **Maven 3.6+**

### Run with H2 (default, no setup needed)

```bash
mvn spring-boot:run
```

The app starts at `http://localhost:8080` with an in-memory H2 database.

### Run with PostgreSQL

```bash
# Start PostgreSQL (e.g. via Docker)
docker run -d --name postgres -e POSTGRES_DB=cleanarchdb -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:16

# Run with postgres profile
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

### Build & Test

```bash
mvn clean install        # Build + run tests
mvn test                 # Run tests only
mvn package -DskipTests  # Build without tests
```

---

## 📡 API Endpoints

| Method   | Endpoint             | Description        |
| -------- | -------------------- | ------------------ |
| `POST`   | `/api/v1/users`      | Create a new user  |
| `GET`    | `/api/v1/users`      | Get all users      |
| `GET`    | `/api/v1/users/{id}` | Get user by ID     |
| `PUT`    | `/api/v1/users/{id}` | Update a user      |
| `DELETE` | `/api/v1/users/{id}` | Delete a user      |

### Example: Create User

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com"
  }'
```

### Swagger UI

Once running, open: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### H2 Console (dev only)

Open: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:mem:cleanarchdb`
- Username: `sa`
- Password: *(empty)*

---

## 🧪 Testing

Unit tests use **JUnit 5**, **Mockito**, and **AssertJ**:

```bash
mvn test
```

Tests are organized by layer:
- `application/service/UserServiceTest.java` — Use case logic tests with mocked ports

---

## 🔧 Key Dependencies

| Dependency       | Purpose                                    |
| ---------------- | ------------------------------------------ |
| Spring Boot 3.3  | Application framework                      |
| Spring Data JPA  | Repository abstraction over Hibernate      |
| Hibernate        | ORM / JPA implementation                   |
| H2               | In-memory database (dev/test)              |
| PostgreSQL       | Production database driver                 |
| MapStruct        | Compile-time object mapping                |
| Lombok           | Boilerplate reduction                      |
| SpringDoc OpenAPI| Swagger UI & API documentation             |
| Bean Validation  | Request DTO validation (`@Valid`)          |

---

## 📚 Extending the Architecture

To add a new entity (e.g. `Product`):

1. **Domain:** Create `Product.java` model + use case interfaces in `port/in/` + repository port in `port/out/`
2. **Application:** Create `ProductService.java` implementing use cases
3. **Infrastructure:** Create `ProductJpaEntity`, `JpaProductRepository`, `ProductRepositoryAdapter`, and `ProductPersistenceMapper`
4. **Presentation:** Create `ProductController`, request/response DTOs, and `ProductApiMapper`

The pattern is always the same — **domain first, infrastructure last**.
