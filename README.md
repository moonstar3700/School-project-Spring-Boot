# Coin Collectors Club Web Application

This project is a Spring Boot web application designed for coin collectors. It includes a standard API with HTML templates and an extension featuring only a REST API without any front end. The goal of this project is to learn how to create applications using Spring Boot.

## Project Overview

The Coin Collectors Club Web Application is developed to manage a collection of coins. The project includes features such as authentication, authorization, CRUD operations, and JPA repository integration. The application uses an H2 database, as connecting to an external database was beyond the scope of this project. 

## Focus on Clean Code and MVC

The project emphasizes writing clean code and following the Model-View-Controller (MVC) architecture to separate concerns and enhance maintainability.

## Features

- **Authentication:** Secure login functionality.
- **Authorization:** Role-based access control.
- **CRUD Operations:** Manage coin collections with create, read, update, and delete functionalities.
- **JPA Repository:** Simplified database interactions.
- **H2 Database:** In-memory database for development and testing.
- **REST API:** Backend services with endpoints for various operations.
- **Postman:** Tool for testing the REST API.
- **JUnit Tests:** Automated testing using Spring Boot JUnit.

## Technologies Used


- **HTML/CSS**
- **Spring Boot:** Framework for building Java-based web applications.
- **H2 Database:** In-memory database used for development.
- **JPA (Java Persistence API):** For data persistence and repository management.
- **Postman:** For testing and interacting with the REST API.
- **Spring Boot JUnit:** For automated testing.
- **IntelliJ IDEA:** Recommended IDE for creating and running the project.

## Setup Instructions

### Prerequisites

- Install [Postman](https://www.postman.com/)

### Running the Application (with Intellij)

1. **Open the Project in IntelliJ IDEA.**

2. **Run the Application:**
   - Navigate to the main class (usually `Application.java`) and run it.

3. **Access the Application:**
   - Open a web browser and navigate to `http://localhost:8080`.

### Testing the REST API with Postman

1. **Get Overview of Coins:**
   - **URL:** `http://localhost:8080/api/coin/overview`
   - **Method:** GET

2. **Add a Coin:**
   - **URL:** `http://localhost:8080/api/coin/add`
   - **Method:** POST
   - **Headers:**
     - Content-Type: application/json
   - **Body (JSON):**
     ```json
     {
       "name": "Dollar",
       "country": "America",
       "currency": 14.0,
       "value": 1.0,
       "year": 1800
     }
     ```

3. **Update a Coin:**
   - **URL:** `http://localhost:8080/api/coin/update/{coin id}`
   - **Method:** PUT
   - **Headers:**
     - Content-Type: application/json
   - **Body (JSON):**
     ```json
     {
       "name": "Dollar",
       "country": "America",
       "currency": 14.0,
       "value": 1.0,
       "year": 1800
     }
     ```

4. **Delete a Coin:**
   - **URL:** `http://localhost:8080/api/coin/delete/{coin id}`
   - **Method:** DELETE
