# URL Shortener API

A clean and secure URL shortening service built with Spring Boot. This application provides a platform for creating manageable links, supporting custom aliases, detailed click tracking, and automated maintenance.

## Technologies & Tools

- **Java 17 & Spring Boot 3**: Leveraging the latest features for a modern backend.
- **Spring Security & JWT**: Authentication and authorization.
- **Spring Data JPA**: Efficient persistence layer with Hibernate.
- **MySQL & Flyway**: Reliable data storage with versioned database migrations.
- **Hibernate Validator**: Strict data integrity through declarative validation.
- **Swagger/OpenAPI 3**: Comprehensive and interactive API documentation.
- **Docker Compose**: Seamless local environment orchestration.
- **JUnit 5 & Mockito**: High-quality code via rigorous unit testing.

## Features

- **User Authentication**: Secure registration and login using JWT-based bearer tokens.
- **Smart Shortening**: Automatic generation of unique short keys using a Base62 algorithm.
- **Custom Aliases**: Users can define their own customized short keys.
- **Click Analytics**: Tracks total clicks and the "Last Clicked" timestamp for every link.
- **Automatic Cleanup**: A scheduled background task automatically prunes links inactive for more than 60 days.
- **Security First**: 
    - URL protocol validation (ensuring safety).
    - Resource ownership protection (users can only manage their own data).
    - Global exception handling for clean and standardized error responses.
- **Future Improvements**:
    - Implement a QRCode Generator.

## Architecture & Best Practices

- **Layered Architecture**: Clear separation between Controllers (Web), Services (Business Logic), and Repositories (Persistence).
- **Domain-Driven Design (DDD) Patterns**: Logic centered around business domains to ensure maintainability.
- **DTO Pattern**: Use of Java Records for immutable data transfer objects, ensuring data encapsulation.
- **Transaction Management**: Robust consistency using Spring's `@Transactional` for complex operations.
- **Scalability**: Designed for high performance with database indexing on short keys and efficient lookup strategies.
- **Clean Code**: Adherence to SOLID principles and industry-standard naming conventions.

## Personal Notes

- Developing this project greatly improved my back-end development skills. It was my first experience working with technologies such as Docker, Flyway, Spring Security, validation, and testing with JUnit/Mockito.
- I’m very proud of how much I learned about what software development really involves. I tried to focus on clean architecture, solid logic, and security. Along the way, several issues appeared — at one point, a user could delete another user, update an admin’s information, and even an admin could change their own role.
- At first, I thought I wouldn’t be able to finish the project, but that turned out to be a mistake. Now I truly understand why everyone says you learn much more by building personal projects.
- I know this isn’t a perfect API, but despite the challenges (and headaches), developing this project was a lot of fun. Thank you all ❤️
