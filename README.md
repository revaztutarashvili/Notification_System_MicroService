# Customer Notification Address Facade System

This repository contains the source code for the **Customer Notification Address Facade System**, a Spring Boot microservice designed to centralize and manage customer contact information and notification preferences. It acts as a unified source for all recipient addresses and delivery statuses, enabling other systems within the ecosystem to efficiently retrieve and update customer delivery data.

## Table of Contents

* [Overview](#overview)
* [Technical Stack](#technical-stack)
* [Key Features](#key-features)
* [Setup and Installation](#setup-and-installation)

  * [Prerequisites](#prerequisites)
  * [Database Setup](#database-setup)
  * [Project Clone and Build](#project-clone-and-build)
  * [Configuration](#configuration)
  * [Running the Application](#running-the-application)
* [Accessing the Application](#accessing-the-application)

  * [Admin Login](#admin-login)
  * [Admin Dashboard](#admin-dashboard)
* [API Endpoints (For External Systems)](#api-endpoints-for-external-systems)
* [Project Structure Overview](#project-structure-overview)

---

## Overview

The **Customer Notification Address Facade System** is a microservice to centralize and manage customer contact information and preferences for notifications. This system serves as a **single source of truth** for customer contact data (email, phone, postal address) and their notification preferences (opt-in/opt-out for various channels). Other services in the ecosystem can consume this data via a RESTful API.

### Key Design Principles

* **Centralized Data**: One unified source for customer contact and preference data.
* **Microservice Architecture**: Designed to be easily integrated into a larger ecosystem. Future enhancements may include integration with message brokers (e.g., Kafka, RabbitMQ) for asynchronous communication.
* **Admin-Managed Customers**: Only system administrators can manage customer data.
* **Secure Administration**: Only authenticated administrators can access the management UI and administrative APIs.

---

## Technical Stack

The project is built using the following technologies:

* **Spring Boot 3.3.12**: Core framework for robust Spring applications.
* **Spring Data JPA**: Simplified data access and persistence with Hibernate.
* **Spring Security**: Authentication and authorization (JWT for APIs, Form Login for UI).
* **Thymeleaf**: Java template engine for web UI development.
* **PostgreSQL**: Relational database for data storage.
* **Maven**: Project management and build automation tool.
* **Lombok**: Reduces boilerplate code (e.g., getters, setters, constructors).
* **ModelMapper**: Object-to-object mapping library.
* **JJWT**: JSON Web Token (JWT) creation and validation for secure API communication.
* **Bootstrap 4 & Tailwind CSS**: Responsive styling for web UI.

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
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.modelmapper</groupId>
			<artifactId>modelmapper</artifactId>
			<version>3.2.0</version> </dependency>
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
```

---

## Key Features

* **Admin Management**:

  * Create, view, update, and delete admin users (requires `ROLE_SUPER_ADMIN`).

* **Customer Information Management**:

  * Add new customer records (full name, email, phone number).
  * View, update, and delete customer details.

* **Address Management**:

  * Add multiple addresses (email, SMS, postal) to a customer.
  * View, update, and delete addresses.

* **Notification Preference Management**:

  * Manage notification opt-in/opt-out for channels (Email, SMS, Promotional).

* **Role-Based Access Control**:

  * `ROLE_SUPER_ADMIN`: Manages admins and customers.
  * `ROLE_ADMIN`: Manages customers.
  * `ROLE_USER`: Deprecated.

* **JWT-based API Security**:

  * Secure RESTful API endpoints with JWT authentication.

* **Form-based UI Security**:

  * Secure web UI with Spring Security’s form login.

---

## Setup and Installation

### Prerequisites

* **Java 17 or higher**: Ensure JDK 17 is installed.
* **Maven**: Version 3.x.x or higher.
* **PostgreSQL**: Database server installed and running.
* **Git**: For cloning the repository.

### Database Setup

1. **Create a PostgreSQL Database**:
   Open your PostgreSQL client (e.g., `psql` or `pgAdmin`) and create a new database.

   ```sql
   CREATE DATABASE customer_notification_db;
   ```

2. **Database Credentials**:
   Update the database credentials in `src/main/resources/application.properties`:

   ```properties
   spring.datasource.username=postgres
   spring.datasource.password=953012
   ```

3. **Database Schema (Automatic)**:
   The application will automatically create/update the database schema when it starts.

### Project Clone and Build

1. **Clone the Repository**:
   Clone the project to your local machine:

   ```bash
   git clone <YOUR_REPOSITORY_URL>
   cd customer_notification_system
   ```

2. **Build the Project**:
   Run Maven to compile and package the application:

   ```bash
   mvn clean install
   ```

### Configuration

The application configuration is in `src/main/resources/application.properties`:

```properties
# Application settings
spring.application.name=customer_notification_system
server.port=8082

# Database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/customer_notification_db
spring.datasource.username=postgres
spring.datasource.password=953012

# JWT Configuration
app.jwt.secret=YourSuperSecretKeyHere
app.jwt.expiration-ms=86400000

# Logging configuration
logging.level.org.springframework.security=DEBUG
```

### Running the Application

Run the Spring Boot application using Maven:

```bash
mvn spring-boot:run
```

The application will start at `http://localhost:8082`.

---

## Accessing the Application

### Admin Login

On the first run, a default Super Admin user is created automatically:

* **URL**: [http://localhost:8082/login](http://localhost:8082/login)
* **Username**: `superadmin`
* **Password**: `123`

After logging in, you will be redirected to the **Admin Dashboard**.

> **Important**: Change the default Super Admin password after the first login in production.

### Admin Dashboard

Once logged in as an admin, navigate to the admin dashboard:

* **URL**: [http://localhost:8082/admin/dashboard](http://localhost:8082/admin/dashboard)

From here, you can manage other features like customers and admins.

---

## API Endpoints (For External Systems)

### Authentication API

**POST /api/auth/login**: Authenticate an admin user and receive a JWT.

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
  "jwt": "your-jwt-token"
}
```

### Customer Management APIs

* **POST /api/customers**: Create a new customer.
* **GET /api/customers**: Get a list of all customers.
* **GET /api/customers/{id}**: Get customer details by ID.
* **PUT /api/customers/{id}**: Update customer.
* **DELETE /api/customers/{id}**: Delete customer.

### Address Management APIs

* **POST /api/addresses**: Add an address for a customer.
* **GET /api/addresses/customer/{customerId}**: Get customer addresses.
* **DELETE /api/addresses/{id}**: Delete an address.

**Note**: Secure all API calls with the JWT in the `Authorization` header:

```bash
Authorization: Bearer <YOUR_JWT_TOKEN>
```

---

## Project Structure Overview

```
src/main/java/com/example/customer_notification_system/
    ├── config/           # Spring Security and app-wide configurations
    ├── controller/       # REST API controllers & Thymeleaf web UI controllers
    ├── dto/              # Data Transfer Objects for requests and responses
    ├── entity/           # JPA entities mapping to database tables
    ├── enums/            # Enumerations for address types, notification channels
    ├── exception/        # Custom exception classes
    ├── mapper/           # Utility classes for entity-to-DTO mapping
    ├── repository/       # Spring Data JPA repositories
    ├── security/         # JWT utilities, custom UserDetails, etc.
    └── service/          # Business logic services

src/main/resources/
    ├── application.properties
    ├── static/           # Static resources (CSS, JS, images)
    └── templates/        # Thymeleaf templates (login.html, admin/, auth/)
```

---
