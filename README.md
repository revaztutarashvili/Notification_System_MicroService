Here's the markdown version of your documentation for GitHub, optimized for better readability and structure:

````markdown
# Customer Notification Address Facade System

This repository contains the source code for the **Customer Notification Address Facade System**, a Spring Boot microservice designed to centralize and manage customer contact information and notification preferences. It serves as a unified source for all recipient addresses and delivery statuses, enabling other systems in the ecosystem to efficiently retrieve and update customer delivery data.

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

## Overview

This project involves creating a **Customer Notification Address Facade System**, a microservice to centralize and manage customer contact information and preferences for notifications. The system acts as a single source of truth for all recipient addresses and delivery statuses, helping other systems in the ecosystem to fetch and update customer delivery data efficiently.

### Key Design Principles:
- **Centralized Data**: Single source for all customer contact and preference data.
- **Microservice Architecture**: Designed to be integrated into a larger ecosystem.
- **Admin-Managed Customers**: Customer data is managed exclusively by system administrators.
- **Secure Administration**: Only authenticated administrators can access the UI and APIs.

## Technical Stack

The project is built on the following technologies:

- **Spring Boot 3.3.12**: Core framework for building stand-alone, production-grade Spring applications.
- **Spring Data JPA**: For simplified data access and persistence with Hibernate.
- **Spring Security**: Authentication and authorization (JWT for APIs, Form Login for UI).
- **Thymeleaf**: Server-side Java template engine for rich web UI.
- **PostgreSQL**: Relational database for data storage.
- **Maven**: Project management and build automation.
- **Lombok**: Reduces boilerplate code (getters, setters, constructors).
- **ModelMapper**: Convention-based object-to-object mapping.
- **JJWT (Java JWT)**: For secure JSON Web Token creation and validation.
- **Jakarta Validation**: Declarative data validation annotations.
- **Bootstrap 4 & Tailwind CSS**: For responsive web UI styling.

## Key Features

- **Admin Management**: Create, view, update, and delete admin users.
- **Customer Information Management**:
  - Add, view, update, and delete customer details (full name, email, phone number).
  - Pagination support for listing customers.
- **Address Management**:
  - Add, view, update, and delete customer addresses (email, SMS, postal).
- **Notification Preference Management**:
  - Manage notification opt-in/opt-out statuses for customers.
- **Role-Based Access Control**:
  - `ROLE_SUPER_ADMIN`: Can manage admins and customers.
  - `ROLE_ADMIN`: Can manage customers.
  - `ROLE_USER`: Deprecated in this system (customers are data objects, not login users).
- **JWT-based API Security**: Secure RESTful APIs with JWT authentication.
- **Form-based UI Security**: Secure UI with Spring Security's form login.

## Setup and Installation

### Prerequisites
- **Java 17 or higher**: Ensure JDK 17 is installed.
- **Maven**: Version 3.x.x or higher.
- **PostgreSQL**: Database server installed and running.
- **Git**: For cloning the repository.

### Database Setup
1. **Create a PostgreSQL Database**:
   ```sql
   CREATE DATABASE customer_notification_db;
````

2. **Update Credentials**:
   Ensure the `application.properties` file matches your PostgreSQL username and password:

   ```properties
   spring.datasource.username=postgres
   spring.datasource.password=953012
   ```

3. **Database Schema**:
   The application will automatically create the database schema when it starts if not already created.

### Project Clone and Build

1. **Clone the Repository**:

   ```bash
   git clone <YOUR_REPOSITORY_URL>
   cd customer_notification_system
   ```
2. **Build the Project**:

   ```bash
   mvn clean install
   ```

### Configuration

The main configuration is located in `src/main/resources/application.properties`:

```properties
spring.application.name=customer_notification_system
server.port=8082
spring.datasource.url=jdbc:postgresql://localhost:5432/customer_notification_db
spring.datasource.username=postgres
spring.datasource.password=953012
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
app.jwt.secret=YourSuperSecretKeyThatIsAtLeast256BitsLongAndShouldBeRandomlyGeneratedInProd
app.jwt.expiration-ms=86400000
```

**Important Notes**:

* **`spring.jpa.hibernate.ddl-auto=create`**: This setting will drop and re-create the schema on each start. Change it to `update` or `none` for production.
* **`app.jwt.secret`**: Change this secret key in production to a strong, randomly generated string.

## Running the Application

You can run the Spring Boot application using Maven:

```bash
mvn spring-boot:run
```

The application will start at [http://localhost:8082](http://localhost:8082).

## Accessing the Application

### Admin Login

Upon the first run, a default Super Admin user will be created automatically.

* **URL**: [http://localhost:8082/login](http://localhost:8082/login)
* **Username**: `superadmin`
* **Password**: `123`

**Note**: It is highly recommended to change the default password after the first login.

### Admin Dashboard

Once logged in, you will be redirected to the Admin Dashboard:

* **URL**: [http://localhost:8082/admin/dashboard](http://localhost:8082/admin/dashboard)

From the dashboard, you can navigate to various management pages (Admins, Customers, etc.).

## API Endpoints (For External Systems)

### Authentication API:

* **POST /api/auth/login**: Authenticate an admin and receive a JWT.

**Request Body**:

```json
{
  "username": "your_username",
  "password": "your_password"
}
```

**Response**:

```json
{
  "token": "YOUR_JWT_TOKEN"
}
```

### Customer Management APIs:

* **POST /api/customers**: Create a new customer.
* **GET /api/customers**: Get a list of all customers.
* **GET /api/customers/{id}**: Get customer details by ID.
* **PUT /api/customers/{id}**: Update customer.
* **DELETE /api/customers/{id}**: Delete customer.

### Address Management APIs:

* **POST /api/addresses**: Add an address for a customer.
* **GET /api/addresses/customer/{customerId}**: Get all addresses for a customer.
* **DELETE /api/addresses/{id}**: Delete an address.

**To use protected API endpoints**, include the JWT in the `Authorization` header:

```http
Authorization: Bearer <YOUR_JWT_TOKEN>
```

## Project Structure Overview

The project follows a standard Spring Boot application structure:

```plaintext
src/main/java/com.example.customer_notification_system/
  ├── config/        # Security and application configurations
  ├── controller/    # API and UI controllers
  ├── dto/           # Data Transfer Objects
  ├── entity/        # JPA entities
  ├── enums/         # Enum classes
  ├── exception/     # Custom exceptions
  ├── mapper/        # Utility classes for mapping entities to DTOs
  ├── repository/    # Spring Data JPA repositories
  ├── security/      # JWT utilities and custom UserDetails
  ├── service/       # Business logic services

src/main/resources/
  ├── application.properties  # Application configuration
  ├── static/                 # Static assets (CSS, JS, images)
  ├── templates/              # Thymeleaf templates (login, admin, etc.)
```

---

Feel free to adjust any placeholder values or configurations as per your requirements.

```

This markdown version makes the documentation clean, structured, and GitHub-friendly with headers, lists, and code blocks. It also ensures the content is easily readable and follows standard Markdown practices for GitHub repositories.
```
