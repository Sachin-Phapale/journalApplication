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
  - Java 17
  - Spring Boot 2.7.16
  - Spring Security
  - Spring Data MongoDB
  - JWT Authentication
  - Redis for caching
  - Apache Kafka for event streaming
  - Swagger/OpenAPI 3.0 for API documentation

- **Frontend**
  - React 18
  - React Router 6
  - TailwindCSS
  - Axios for API calls
  - Lucide React for icons

- **Database**
  - MongoDB (NoSQL Database)
  - Redis (Caching)

- **DevOps & Tools**
  - Maven (Dependency Management)
  - Node.js & npm
  - Lombok (Reducing Boilerplate)
  - Logback (Logging)

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Node.js 16 or higher
- npm 8 or higher
- MongoDB 4.4 or higher
- Redis 6.x or higher (optional)
- Apache Kafka 2.8 or higher (optional)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Sachin-Phapale/journalApplication.git
   cd journalApplication
   ```

2. **Set up the Backend**
   - Configure environment variables or update `src/main/resources/application.yml`
   - Set up MongoDB connection string
   - Configure JWT secret and other settings

3. **Set up the Frontend**
   ```bash
   cd frontend
   npm install
   ```

4. **Build the Frontend**
   ```bash
   npm run build
   ```
   
   This will automatically copy the build files to the Spring Boot static directory.

5. **Run the application**
   ```bash
   cd ..
   ./mvnw.cmd spring-boot:run
   ```

6. **Access the application**
   - Frontend Application: http://localhost:8080
   - API Documentation: http://localhost:8080/swagger-ui.html

### Development Mode

For development with hot reload:

1. **Start Backend** (in one terminal):
   ```bash
   ./mvnw.cmd spring-boot:run
   ```

2. **Start Frontend** (in another terminal):
   ```bash
   cd frontend
   npm start
   ```

The frontend will run on http://localhost:3000 and proxy API calls to the backend.

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
