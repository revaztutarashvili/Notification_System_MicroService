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
