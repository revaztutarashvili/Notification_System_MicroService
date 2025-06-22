Here's your documentation in Markdown format, structured to look clean and organized on GitHub:

````markdown
# Customer Notification Address Facade System

This repository contains the source code for the **Customer Notification Address Facade System**, a Spring Boot microservice designed to centralize and manage customer contact information and notification preferences. It acts as a unified source for all recipient addresses and delivery statuses, enabling other systems within the ecosystem to efficiently retrieve and update customer delivery data.

---

## Table of Contents
1. [Overview](#overview)
2. [Technical Stack](#technical-stack)
3. [Key Features](#key-features)
4. [Setup and Installation](#setup-and-installation)
   - [Prerequisites](#prerequisites)
   - [Database Setup](#database-setup)
   - [Project Clone and Build](#project-clone-and-build)
   - [Configuration](#configuration)
5. [Running the Application](#running-the-application)
6. [Accessing the Application](#accessing-the-application)
   - [Admin Login](#admin-login)
   - [Admin Dashboard](#admin-dashboard)
7. [API Endpoints (For External Systems)](#api-endpoints-for-external-systems)
8. [Project Structure Overview](#project-structure-overview)

---

## 1. Overview
This project involves creating a **Customer Notification Address Facade System**, a microservice to centralize and manage customer contact information and preferences for notifications. This system will act as a single source of truth for all recipient addresses and delivery statuses, helping other systems in the ecosystem to fetch and update customer delivery data efficiently.

The **Customer Notification Address Facade System** is a backend microservice responsible for maintaining a definitive source of truth for customer contact details (email, phone, postal address) and their notification preferences (opt-in/opt-out for various channels). This system is designed to be consumed by other services through a RESTful API, providing a robust and centralized mechanism for managing recipient data crucial for notification delivery.

### Key Design Principles:
- **Centralized Data:** Single source for all customer contact and preference data.
- **Microservice Architecture:** Designed to be easily integrated into a larger ecosystem.
- **Admin-Managed Customers:** Customer data is added, updated, and deleted exclusively by system administrators.
- **Secure Administration:** Only authenticated administrators can access the management UI and administrative APIs.

---

## 2. Technical Stack
The project is built on the following technologies:

- **Spring Boot 3.3.12:** Core framework for building robust, stand-alone, production-grade Spring applications.
- **Spring Data JPA:** For simplified data access and persistence with Hibernate.
- **Spring Security:** For authentication and authorization (JWT for APIs, Form Login for UI).
- **Thymeleaf:** For web UI development (Admin pages).
- **PostgreSQL:** Relational database for data storage.
- **Maven:** Project management and build automation tool.
- **Lombok:** Reduces boilerplate code (e.g., getters, setters, constructors).
- **ModelMapper:** Convention-based object-to-object mapping library.
- **JJWT (Java JWT):** For secure API communication.
- **Jakarta Validation:** For declarative data validation (e.g., `@NotBlank`, `@Email`).
- **Bootstrap 4 & Tailwind CSS:** For responsive and modern styling of the web UI.

### Dependencies (from `pom.xml`):
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <!-- Add other dependencies as listed in the original document -->
</dependencies>
````

---

## 3. Key Features

### Admin Management:

* Create, view, update, and delete admin users (requires `ROLE_SUPER_ADMIN`).

### Customer Information Management:

* Add new customer records with full name, email, and phone number.
* View all customers in a paginated list.
* Update existing customer details.
* Delete customer records.

### Address Management:

* Add multiple addresses (Email, SMS, Postal) for a customer.
* View all addresses associated with a customer.
* Update and delete specific addresses for a customer.

### Notification Preference Management:

* Manage opt-in/opt-out status for each customer across different channels (Email, SMS, Promotional).

### Role-Based Access Control:

* **ROLE\_SUPER\_ADMIN:** Can manage admins and customers.
* **ROLE\_ADMIN:** Can manage customers.
* **ROLE\_USER:** Deprecated for customers in this system.

### JWT-based API Security:

* Secure RESTful API endpoints using JWT.

### Form-based UI Security:

* Secure web UI using Spring Security's form login.

---

## 4. Setup and Installation

Follow these steps to get the project up and running on your local machine.

### Prerequisites:

* **Java 17 or higher:** Ensure the JDK is installed.
* **Maven 3.x.x or higher.**
* **PostgreSQL:** Ensure the database server is installed and running.
* **Git:** For cloning the repository.

### Database Setup:

1. **Create a PostgreSQL Database:**

   ```sql
   CREATE DATABASE customer_notification_db;
   ```

2. **Database Credentials:**
   Ensure your `application.properties` matches your PostgreSQL credentials.

   ```properties
   spring.datasource.username=postgres
   spring.datasource.password=953012
   ```

3. **Database Schema:**
   The application will automatically create/update the database schema on the first run.

---

### Project Clone and Build:

1. **Clone the Repository:**

   ```bash
   git clone <YOUR_REPOSITORY_URL>
   cd customer_notification_system
   ```

2. **Build the Project:**

   ```bash
   mvn clean install
   ```

### Configuration:

The main configuration file is located in `src/main/resources/application.properties`.

```properties
spring.application.name=customer_notification_system
server.port=8082

# Database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/customer_notification_db
spring.datasource.username=postgres
spring.datasource.password=953012
spring.jpa.hibernate.ddl-auto=create

# JWT Configuration
app.jwt.secret=YourSuperSecretKey
app.jwt.expiration-ms=86400000

# Logging
logging.level.org.springframework.security=DEBUG
```

---

## 5. Running the Application

You can run the Spring Boot application using Maven:

```bash
mvn spring-boot:run
```

The application will be available at: `http://localhost:8082`.

---

## 6. Accessing the Application

### Admin Login:

* **URL:** `http://localhost:8082/login`
* **Default Super Admin Username:** `superadmin`
* **Default Super Admin Password:** `123`

### Admin Dashboard:

After logging in, you will be redirected to the admin dashboard at:

`http://localhost:8082/admin/dashboard`

From the dashboard, you can manage admins, customers, and other settings.

---

## 7. API Endpoints (For External Systems)

### Authentication API:

* **POST /api/auth/login**: Authenticate an admin user and receive a JWT.

  * **Request Body:**

    ```json
    {
      "username": "your_username",
      "password": "your_password"
    }
    ```
  * **Response:** JWT token.

### Customer Management APIs:

* **POST /api/customers**: Create a new customer.
* **GET /api/customers**: Get a list of all customers.
* **GET /api/customers/{id}**: Get customer details by ID.
* **PUT /api/customers/{id}**: Update customer.
* **DELETE /api/customers/{id}**: Delete customer.

### Address Management APIs:

* **POST /api/addresses**: Add an address for a customer.
* **GET /api/addresses/customer/{customerId}**: Get customer addresses.
* **DELETE /api/addresses/{id}**: Delete an address.

### JWT Usage:

Include the JWT token in the `Authorization` header for protected endpoints:

```bash
Authorization: Bearer <YOUR_JWT_TOKEN>
```

---

## 8. Project Structure Overview

The project follows a standard Spring Boot structure:

```
src/main/java/com/example/customer_notification_system/
    ├── config/        # Spring Security and app-wide configurations
    ├── controller/    # REST API controllers & Thymeleaf web UI controllers
    ├── dto/           # Data Transfer Objects for requests and responses
    ├── entity/        # JPA entities mapping to database tables
    ├── enums/         # Enumerations for address types, notification channels
    ├── exception/     # Custom exception classes
    ├── mapper/        # Utility classes for entity-to-DTO mapping
    ├── repository/    # Spring Data JPA repositories
    ├── security/      # JWT utilities, custom UserDetails, etc.
    └── service/       # Business logic services

src/main/resources/
    ├── application.properties
    ├── static/        # Static resources (CSS, JS)
    └── templates/     # Thymeleaf templates (e.g., login.html, admin/)
```

---

Now
