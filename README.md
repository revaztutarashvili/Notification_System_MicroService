# Customer Notification Address Facade System

This repository contains the source code for the Customer Notification Address Facade System, a Spring Boot microservice designed to centralize and manage customer contact information and notification preferences. It acts as a unified source for all recipient addresses and delivery statuses, enabling other systems within the ecosystem to efficiently retrieve and update customer delivery data.

## Table of Contents

1.  [Overview](#1-overview)
2.  [Technical Stack](#2-technical-stack)
3.  [Key Features](#3-key-features)
4.  [Setup and Installation](#4-setup-and-installation)
    * [Prerequisites](#prerequisites)
    * [Database Setup](#database-setup)
    * [Project Clone and Build](#project-clone-and-build)
    * [Configuration](#configuration)
5.  [Running the Application](#5-running-the-application)
6.  [Accessing the Application](#6-accessing-the-application)
    * [Admin Login](#admin-login)
    * [Admin Dashboard](#admin-dashboard)
7.  [API Endpoints (For External Systems)](#7-api-endpoints-for-external-systems)
8.  [Project Structure Overview](#8-project-structure-overview)

---

## 1. Overview

This project involves creating a Customer Notification Address Facade System, a microservice to centralize and manage customer contact information and preferences for notifications. This system will act as a single source of truth for all recipient addresses and delivery statuses, helping other systems in the ecosystem to fetch and update customer delivery data efficiently.

The Customer Notification Address Facade System is a backend microservice responsible for maintaining a definitive source of truth for customer contact details (email, phone, postal address) and their notification preferences (opt-in/opt-out for various channels). This system is designed to be consumed by other services through a RESTful API, providing a robust and centralized mechanism for managing recipient data crucial for notification delivery.

**Key Design Principles:**
* **Centralized Data:** Single source for all customer contact and preference data.
* **Microservice Architecture:** Designed to be easily integrated into a larger ecosystem. Future enhancements may include integration with message brokers (e.g., Kafka, RabbitMQ) for asynchronous communication and event-driven architectures.
* **Admin-Managed Customers:** Customer data is added, updated, and deleted exclusively by system administrators. Customers **do not** have login capabilities to this system.
* **Secure Administration:** Only authenticated administrators can access the management UI and administrative APIs.

## 2. Technical Stack

The project is built on the following technologies:

* **Spring Boot 3.3.12:** The core framework for building robust, stand-alone, production-grade Spring applications.
* **Spring Data JPA:** For simplified data access and persistence with Hibernate.
* **Spring Security:** Provides comprehensive security services for authentication and authorization (JWT for APIs, Form Login for UI).
* **Thymeleaf:** A server-side Java template engine for rich web UI development (used for Admin web pages).
* **PostgreSQL:** The relational database used for data storage.
* **Maven:** Project management and build automation tool.
* **Lombok:** A library to reduce boilerplate code (e.g., getters, setters, constructors).
* **ModelMapper:** A convention-based object-to-object mapping library.
* **JJWT (Java JWT):** For JSON Web Token (JWT) creation and validation for secure API communication.
* **Jakarta Validation:** For declarative data validation (e.g., `@NotBlank`, `@Email`).
* **Bootstrap 4 & Tailwind CSS:** For responsive and modern styling of the web UI.

**Dependencies (from `pom.xml`):**

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
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.modelmapper</groupId>
        <artifactId>modelmapper</artifactId>
        <version>3.2.0</version>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>

3. Key Features
Admin Management:
Create, view, update, and delete admin users – Administrators with the ROLE_SUPER_ADMIN role can manage admin users in the system.

Customer Information Management:
Add new customer records – Add customer details such as full name, email, and phone number.

View all customers – Customers are displayed in a paginated list.

Update customer details – Modify customer information as needed.

Delete customer records – Remove customer information from the system.

Address Management:
Add multiple addresses for a customer – You can associate several addresses (Email, SMS, Postal) to a customer.

View customer addresses – See all addresses linked to a specific customer.

Update and delete specific addresses – Modify or remove individual addresses for a customer.

Notification Preference Management:
Manage opt-in/opt-out status for notification channels – Admins can set notification preferences (opt-in/opt-out) for each customer across various channels, such as Email, SMS, and Promotional.

Role-Based Access Control:
ROLE_SUPER_ADMIN – Full access to manage admins and customers.

ROLE_ADMIN – Limited to managing customer records.

ROLE_USER – This role is deprecated for customers, as the system does not require customers to log in.

JWT-based API Security:
Secure RESTful API endpoints – Use JSON Web Tokens (JWT) to protect and authenticate API requests.

Form-based UI Security:
Secure web UI – The application uses Spring Security’s form-based login for admin access, ensuring only authenticated users can interact with the admin dashboard.---

# 4. Setup and Installation

Follow these steps to get the project up and running on your local machine.

## Prerequisites:

**Java 17 or higher:** Ensure the JDK is installed.
**Maven 3.x.x or higher.**
**PostgreSQL:** Ensure the database server is installed and running.
**Git:** For cloning the repository.

## Database Setup:

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
