# RESTful API Implementation

This project implements a RESTful API for managing users. It provides endpoints to perform CRUD operations on user resources.

## Project Structure

The project structure is as follows:
- `src/main/java`: Contains the Java source code
    - `com.example.clearsolutiontesttask`: Main package for the application
        - `controller`: Contains the REST controllers for handling HTTP requests
        - `dto`: Contains Data Transfer Objects (DTOs) used for transferring data between layers
        - `entity`: Contains the entity classes representing the domain model
        - `exception`: Contains custom exception classes
        - `mapper`: Contains mapper classes for mapping between DTOs and entities
        - `service`: Contains the service layer classes for business logic
        - `validator`: Contains validator classes for validating user input
- `src/test/java`: Contains the unit tests for the application

## Requirements
The project fulfills the following requirements:
1. Implements CRUD operations for user resources with fields: Email, First name, Last name, Birth date, Address, Phone number.
2. Validates user input against specified requirements.
3. Covers code with unit tests using Spring testing framework.
4. Implements error handling for RESTful API.
5. Returns API responses in JSON format.
6. Uses Spring Boot for the web application.
7. Java version: (Specify the Java version used)
8. Spring Boot version: (Specify the Spring Boot version used)

## How to Run
To run the project, follow these steps:
1. Clone the repository to your local machine.
2. Navigate to the project directory.
3. Run the application using `mvn spring-boot:run` or run the main class `ClearSolutionTestTaskApplication.java`.

## API Endpoints
- POST `/users`: Create a new user.
- PUT `/users/{id}`: Update all fields of an existing user.
- PATCH `/users/{id}`: Update specific fields of an existing user.
- DELETE `/users/{id}`: Delete a user by ID.
- GET `/users/age`: Search for users by birth date range.

## Testing
The project includes unit tests for the service layer and controller layer. To run the tests, use `mvn test`.
