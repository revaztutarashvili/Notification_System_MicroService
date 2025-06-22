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
Admin Management: Create, view, update, and delete admin users (requires ROLE_SUPER_ADMIN).
Customer Information Management:
Add new customer records with full name, email, and phone number.
View all customers in a paginated list.
Update existing customer details.
Delete customer records.
Address Management:
Add multiple addresses (Email, SMS, Postal) to a specific customer.
View all addresses associated with a customer.
Update and delete specific addresses for a customer (functionality for these might need further implementation in controllers/services).
Notification Preference Management:
Manage notification opt-in/opt-out status for each customer across different channels (Email, SMS, Promotional). (Functionality for this would typically be accessible from the customer details or a dedicated preferences page).
Role-Based Access Control:
ROLE_SUPER_ADMIN: Can manage admins and customers.
ROLE_ADMIN: Can manage customers.
ROLE_USER: (Deprecated for customers in this system - customers are data objects, not login users).
JWT-based API Security: Secure RESTful API endpoints using JSON Web Tokens.
Form-based UI Security: Secure web UI using Spring Security's form login.
4. Setup and Installation
Follow these steps to get the project up and running on your local machine.

Prerequisites
Java 17 or higher: Ensure Java Development Kit (JDK) 17 is installed.
Maven: Version 3.x.x or higher.
PostgreSQL: Database server installed and running.
Git: For cloning the repository.
Database Setup
Create a PostgreSQL Database:
Open your PostgreSQL client (e.g., psql or pgAdmin) and create a new database.

SQL

CREATE DATABASE customer_notification_db;
Database Credentials:
Ensure your application.properties matches your PostgreSQL username and password. By default, it's configured as:

Properties

spring.datasource.username=postgres
spring.datasource.password=953012
If your PostgreSQL setup uses different credentials, update these lines in src/main/resources/application.properties.

Database Schema (Automatic):
The application is configured to automatically create/update the database schema using spring.jpa.hibernate.ddl-auto=create. This means tables will be created automatically when the application starts for the first time.

Project Clone and Build
Clone the Repository:
Open your terminal or Git Bash and clone the repository to your local machine:

Bash

git clone <YOUR_REPOSITORY_URL>
cd customer_notification_system
Replace <YOUR_REPOSITORY_URL> with the actual URL of your Git repository.

Build the Project:
Navigate to the root directory of the cloned project (customer_notification_system) and build it using Maven:

Bash

mvn clean install
This command compiles the code, runs tests, and packages the application into a JAR file.

Configuration
The main configuration for the application is located in src/main/resources/application.properties.

Properties

spring.application.name=customer_notification_system
server.port=8082

# Database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/customer_notification_db
spring.datasource.username=postgres
spring.datasource.password=953012

# JPA configuration
spring.jpa.hibernate.ddl-auto=create # This will drop and re-create schema on every restart! Change to 'update' or 'none' for production.
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT Configuration
app.jwt.secret=YourSuperSecretKeyThatIsAtLeast256BitsLongAndShouldBeRandomlyGeneratedInProd # IMPORTANT: CHANGE THIS IN PRODUCTION!
app.jwt.expiration-ms=86400000 # 24 hours in milliseconds

# Logging level for Spring Security (can be set to INFO for less verbosity)
logging.level.org.springframework.security=DEBUG
Important:

spring.jpa.hibernate.ddl-auto=create: This setting will drop all existing tables and re-create them every time the application starts. This is suitable for development but should be changed to update or none for production environments to prevent data loss.
app.jwt.secret: Change this secret key in a production environment. Generate a strong, random string that is at least 256 bits long.
5. Running the Application
You can run the Spring Boot application using Maven:

Bash

mvn spring-boot:run
The application will start on http://localhost:8082.

6. Accessing the Application
Admin Login
Upon the first run of the application, if no admin users exist in the database, a default Super Admin user will be created automatically.

URL: http://localhost:8082/login
Default Super Admin Username: superadmin
Default Super Admin Password: 123
You can log in using these credentials. Upon successful login, you will be redirected to the Admin Dashboard.

Important: It is highly recommended to change the default super admin password after the first login in a production environment.

Admin Dashboard
After logging in as an admin, you will be redirected to the admin dashboard.

URL: http://localhost:8082/admin/dashboard (you will be redirected here after successful login).
From the dashboard, you can navigate to other admin management pages (Admins, Customers, etc.).

7. API Endpoints (For External Systems)
The system exposes RESTful APIs primarily for other microservices to consume. These endpoints are protected by JWT authentication (for non-public ones).

Authentication API:
POST /api/auth/login: Authenticate an admin user and receive a JWT.
Request Body (JSON): { "username": "your_username", "password": "your_password" }
Response: JWT token for subsequent API calls.
Customer Management APIs:
POST /api/customers: Create a new customer.
GET /api/customers: Get a list of all customers.
GET /api/customers/{id}: Get customer details by ID.
PUT /api/customers/{id}: Update customer details by ID.
DELETE /api/customers/{id}: Delete a customer by ID.
Address Management APIs:
POST /api/addresses: Add a new address for a customer.
GET /api/addresses/customer/{customerId}: Get all addresses for a specific customer.
DELETE /api/addresses/{id}: Delete an address by ID.
(Further API endpoints for Notification Preferences and Admin Management are also available as defined in respective controllers).
To use protected API endpoints:
Include the JWT received from /api/auth/login in the Authorization header of your requests:
Authorization: Bearer <YOUR_JWT_TOKEN>

8. Project Structure Overview
The project follows a standard Spring Boot application structure:

src/main/java/com/example/customer_notification_system/:
config/: Spring Security and application-wide configurations.
controller/: REST API controllers and Web UI controllers (Thymeleaf).
dto/: Data Transfer Objects (DTOs) for requests and responses.
entity/: JPA entities mapping to database tables.
enums/: Enumerations for address types, notification channels etc.
exception/: Custom exception classes.
mapper/: Utility classes for mapping entities to DTOs.
repository/: Spring Data JPA repositories for database interaction.
security/: JWT utilities, custom UserDetails, and UserDetailsService implementation.
service/: Business logic services and their implementations.
src/main/resources/:
application.properties: Application configuration properties.
static/: Static web resources (CSS, JS, images).
templates/: Thymeleaf HTML templates (e.g., login.html, admin/, auth/).
<!-- end list -->

