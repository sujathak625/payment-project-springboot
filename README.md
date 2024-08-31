# payment-project-springboot

Payment Transaction Management API

Overview

This project is a Spring Boot application that provides an API to manage payment transactions. It handles various operations such as inserting transaction details, verifying device authenticity, and detecting fraud in transactions. The application is structured with proper layers including controllers, services, and DAOs.
Features

    Insert Transaction: Adds a new transaction after verifying the device authenticity.
    Device Detection: Checks if the transaction is being triggered from an authenticated device.
    Fraud Detection: Detects and blocks a card if multiple transactions of the same amount are detected within a short period.

Technologies Used

    Java: Programming language.
    Spring Boot: Framework for building the application.
    H2 Database: In-memory database used for simplicity.
    JUnit: For unit testing.
    RestTemplate: For making REST API calls between microservices.
    Spring Data JPA: For data persistence.

Installation
Prerequisites

    Java 17 or higher
    Maven
    Git