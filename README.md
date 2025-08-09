# Journal Application

A secure, cloud-based journaling application with sentiment analysis and real-time weather integration. This application provides end-to-end encrypted journal entries, user authentication, and a RESTful API for managing personal journal entries.

## 🌟 Features

- **Secure User Authentication**
  - JWT-based authentication
  - OAuth2.0 integration with Google
  - Role-based access control (User & Admin)

- **Journal Management**
  - Create, read, update, and delete journal entries
  - Rich text formatting support
  - Sentiment analysis of entries
  - Entry categorization and tagging

- **Advanced Features**
  - Real-time weather data integration
  - Email notifications and reminders
  - Sentiment tracking over time
  - Redis caching for improved performance
  - Kafka integration for asynchronous processing

- **Admin Dashboard**
  - User management
  - System monitoring
  - Application configuration

## 🛠️ Technologies Used

- **Backend**
  - Java 8
  - Spring Boot 2.7.16
  - Spring Security
  - Spring Data MongoDB
  - JWT Authentication
  - Redis for caching
  - Apache Kafka for event streaming
  - Swagger/OpenAPI 3.0 for API documentation

- **Database**
  - MongoDB (NoSQL Database)
  - Redis (Caching)

- **DevOps & Tools**
  - Maven (Dependency Management)
  - SonarCloud (Code Quality)
  - Lombok (Reducing Boilerplate)
  - Logback (Logging)

## 🚀 Getting Started

### Prerequisites

- Java 8 or higher
- Maven 3.6 or higher
- MongoDB 4.4 or higher
- Redis 6.x or higher
- Apache Kafka 2.8 or higher

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/journal-application.git
   cd journal-application
   ```

2. **Configure the application**
   - Copy `src/main/resources/application.yml.example` to `src/main/resources/application.yml`
   - Update the configuration with your database credentials, JWT secret, and other settings

3. **Build the application**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application**
   - API Documentation: http://localhost:8080/swagger-ui.html
   - Application: http://localhost:8080

## 📚 API Documentation

Once the application is running, you can access the interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

## 🔒 Security

- All API endpoints are secured with JWT authentication
- Passwords are hashed using BCrypt
- CSRF protection enabled
- Rate limiting implemented for authentication endpoints
- Input validation on all endpoints

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Spring Boot Team for the amazing framework
- MongoDB for the database
- Redis for caching
- Apache Kafka for event streaming
- All open-source libraries used in this project
