# DineMaster Pro - Backend

DineMaster Pro is the backend system powering Canim's online ordering and delivery platform. Designed to support role-based access, real-time order updates, and seamless integration with external services, it enhances the restaurant's operational efficiency and customer satisfaction.

---
## Link to application
https://dinemasterpro.netlify.app/

---
## Purpose

Canim is a beloved local restaurant known for its exceptional cuisine and welcoming ambiance. With growing demand, particularly during peak hours, the restaurant faces challenges with its dine-in-only service model. This project aims to address these challenges by implementing an online ordering and delivery system, allowing customers to conveniently order and track their meals from home.

---

## Table of Contents

1. [Features](#features)
2. [Technologies Used](#technologies-used)
3. [Tests](#tests)
4. [Requirements](#requirements)
5. [Documentation](#documentation)
6. [Setup and Installation](#setup-and-installation)
7. [API Overview](#api-overview)
8. [Continuous Integration/Deployment](#continuous-integrationdeployment)
9. [Security](#security)
10. [Contributing](#contributing)
11. [License](#license)

---

## Features

- **`Role-Based Access`**: Provides access control for customers, managers, kitchen staff, and delivery personnel.
- **Order Management**: Real-time order updates and tracking.
- **Menu Management**: `CRUD` operations for managing menu items.
- **Database Migrations**: Version-controlled schema management with Flyway.
- **Location Services**: Geocoding and routing integration for delivery.
- **`Dynamic Delivery Fee Calculation`**: The delivery fee is calculated based on the distance between the restaurant and the user-provided address. The restaurant does not offer deliveries outside of the delivery range (20km). 
- **Secure `Email` Functionality**: Password recovery using SMTP Gmail service.
- **Scalable Architecture**: Built using SOLID principles for maintainability and scalability.

---

## Technologies Used

### Tech Stack

- **Main Language**: `Java` (`Spring Boot`)
- **Database**: `MySQL` (`Flyway` for database migrations)
- **ORM**: `Hibernate` (`JPA` for data access)
- **Authentication**: `JWT` (JSON Web Tokens)
- **Image Storage**: `Cloudinary`
- **Containerization**: `Docker`
- **`Geolocation Services`**: OpenCage Geocoding, Geoapify
- **Email Service**: `SMTP Gmail Service`
- **CI/CD**: GitLab `CI/CD Pipeline`
- **Testing**: `Mockito`, `JUnit` in-memory databases for persistence tests

### **Java Spring Boot**
Java Spring Boot is the core framework used to build scalable and maintainable `RESTful` web services. It naturally supports `SOLID` principles:
- **Single Responsibility Principle (`SRP`)**: Separation of concerns into layers like controllers, services, and persistence.
- **Open-Closed Principle (`OCP`)**: Components are extensible without modifying existing code.
- **Liskov Substitution Principle (`LSP`)**: Interface-based development allows flexible component replacement.
- **Interface Segregation Principle (`ISP`)**: Specific interfaces for distinct use cases ensure modularity.
- **Dependency Inversion Principle (`DIP`)**: Uses IoC and dependency injection for flexibility.

### **JPA and Hibernate**
- The application uses **`Spring Data JPA`** for Object-Relational Mapping (`ORM`), enabling seamless database interaction.
- **`Hibernate`** serves as the ORM provider, simplifying data persistence and retrieval.
- JPA and Hibernate ensure clean and efficient data access by abstracting `SQL complexities`, improving maintainability.

### **MySQL**
Used for structured data storage due to its reliability, performance, and ability to handle complex queries.
- **Entity-Relationship Diagram (ERD)** is provided in the attached design documents.

### **Flyway**
Manages database schema migrations to ensure consistency across environments. Tracks and automates schema changes for seamless deployments.

### **Cloudinary**
Handles image storage and optimization. The backend uploads images securely, while the frontend retrieves them. Features include:
- Scalable cloud storage.
- Automatic image optimization to improve performance.

### **Docker**
- `Docker` is used to containerize the application, ensuring consistency across environments.
- The `CI/CD pipeline` builds and deploys a Docker image for easy integration into various deployment setups.

### **JWT Authentication**
- The application uses **JSON Web Tokens (JWT)** for secure authentication and session management.
- **Access Tokens**: Valid for 20 minutes and used to authenticate API requests.
- **Refresh Tokens**: Valid for 60 minutes and used to renew expired access tokens without requiring re-login.
- This implementation ensures secure, stateless authentication while maintaining user convenience.
- Automatic reuse detection is also implemented : https://auth0.com/blog/refresh-tokens-what-are-they-and-when-to-use-them/#Refresh-Token-Automatic-Reuse-Detection

### **External APIs**
- **OpenCage Geocoding**: Provides reliable geocoding and reverse geocoding using OpenStreetMap data.
- **Geoapify**: Calculates distances between locations for delivery route planning, supporting multiple transport types.

### **SMTP Gmail Service**
Simplifies email functionality, such as sending password recovery emails, without requiring a dedicated email server.

---

## Tests

### Overview
The application is rigorously tested to ensure reliability and correctness:
- **Business Layer Tests**: Over `80%`` of the business logic has been covered by unit tests, ensuring that critical functionality works as expected.
- **Controller Tests**: Tested using `@WithMockUser` to verify API endpoints with various roles and permissions.
- **Persistence Tests**: Repository tests are conducted using an in-memory database to validate data storage and retrieval operations.

### SonarQube Dashboard
The codebase achieves high-quality standards, as verified by SonarQube's static code analysis. The SonarQube gateway passes with all required metrics met.

<img width="1494" alt="Screenshot 2025-01-20 at 10 53 51" src="https://github.com/user-attachments/assets/9a38ff38-1b77-4f2c-b0b2-f40d80ddb9fc" />


---

## Requirements

- **Java Development Kit (JDK)**: 17 or higher
- **Gradle**: For dependency management
- **MySQL**: Version 8.0 or higher
- **Docker**: For containerization
- **GitLab Runner**: For CI/CD pipelines

---

## Documentation
* in /Documents

- **Design Document**
  * All architectural decisions made
  * All tools used
  * `C4` diagrams
  * `ERD`
  * `UML` diagram
  * `Git branching strategy`
  * `CI/CD` Pipeline explanation
- **Web Performance Report**
  * `Lighthouse` performance results
  * `JMeter` performance results
- **Project Plan**
  * Description of what the project is about
  * Description of the problem it solves
  * Other relevant to the project information
- **Security Report**
  * Report on the application meeting the top `10 OWASP prinisples`

---

## Setup and Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-repo/dinemaster-backend.git

   
## API Overview

The backend exposes a RESTful API with the following endpoints:

| Method | Endpoint                     | Description                         | Roles Allowed      |
|--------|------------------------------|-------------------------------------|--------------------|
| `POST` | `/auth/login`                | Authenticate user and issue tokens | All                |
| `GET`  | `/menu`                      | Retrieve the menu items             | Customer, Manager  |
| `POST` | `/menu`                      | Add a new menu item                 | Manager            |
| `PUT`  | `/menu/{id}`                 | Update an existing menu item        | Manager            |
| `DELETE` | `/menu/{id}`               | Delete a menu item                  | Manager            |
| `POST` | `/order`                     | Place a new order                   | Customer           |
| `GET`  | `/orders`                    | Retrieve all orders                 | Manager, Kitchen   |
| `GET`  | `/orders/{id}`               | Retrieve specific order details     | Customer, Manager  |

For detailed API documentation, you can use the Swagger UI available at `/swagger-ui.html` once the application is running.

---

## Continuous Integration/Deployment

The project follows a CI/CD pipeline to automate testing, code quality checks, and deployment. The pipeline consists of the following stages:

1. **Build**: Compiles the code and assembles the application.
2. **Test**: Runs unit and integration tests to ensure correctness.
3. **SonarQube**: Static code analysis is performed to check for code quality and ensure adherence to best practices.
4. **Docker**: Builds and deploys a Docker image of the backend application for containerized deployment.
   
![Backend pipeline](https://github.com/user-attachments/assets/9a1661f2-879e-45c9-94d8-9829a1bfd5c8)


* The yaml file can be found in the root directory of the project 

## Security
- The application uses **JSON Web Tokens (JWT)** for secure authentication and session management.
- **Access Tokens**: Valid for 20 minutes and used to authenticate API requests.
- **Refresh Tokens**: Valid for 60 minutes and used to renew expired access tokens without requiring re-login.
- This implementation ensures secure, stateless authentication while maintaining user convenience.
- Automatic reuse detection is also implemented : https://auth0.com/blog/refresh-tokens-what-are-they-and-when-to-use-them/#Refresh-Token-Automatic-Reuse-Detection

## Contributors

- **Samuil Kozarov**: Software Engineering student at Fontys University of Applied Sciences.

---

## License

This project is licensed under the MIT License.

---

## Disclaimer

This project was developed for educational purposes as part of my coursework at Fontys University of Applied Sciences.

