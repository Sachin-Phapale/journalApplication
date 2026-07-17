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

- **Model Context Protocol (MCP) Integration**
  - Built-in MCP server support over stdio
  - 12 production-ready tools for AI agent pair-programming
  - Seamless authentication integration with Spring Security

- **Admin Dashboard**
  - User management
  - System monitoring
  - Application configuration

## 🛠️ Technologies Used

- **Backend**
  - Java 17
  - Spring Boot 2.7.16
  - Model Context Protocol (MCP) Java SDK (version 1.1.3)
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

## 📁 Project Structure

Below is the directory layout showing both the React frontend and Spring Boot backend (with the new Model Context Protocol configuration):

```text
journalApplication/
├── .github/                       # GitHub workflows and issue templates
├── .mvn/                          # Maven wrapper binaries
├── frontend/                      # React frontend SPA
│   ├── public/                    # Static assets & index.html
│   └── src/                       # React source directory
│       ├── components/            # Reusable UI elements (Navbar, ThemeToggler, etc.)
│       ├── contexts/              # React context states (Auth, Theme)
│       ├── pages/                 # Full pages (Dashboard, Admin, Login, Profile)
│       └── services/              # API Client (Axios configuration)
├── src/                           # Spring Boot backend application
│   ├── main/
│   │   ├── java/net/engineeringdigest/journalApp/
│   │   │   ├── api/               # Third-party integrations (e.g. Weather API)
│   │   │   ├── cache/             # Redis caching layer configuration
│   │   │   ├── config/            # App configs (Security, WebMvc, Swagger)
│   │   │   ├── controller/        # REST controllers (User, Journal, Admin, Public)
│   │   │   ├── dto/               # Data Transfer Objects
│   │   │   ├── entity/            # MongoDB schema models
│   │   │   ├── filter/            # JWT authentication filters
│   │   │   ├── mcp/               # Model Context Protocol (MCP) server & tools
│   │   │   │   ├── config/        # Stdio server setup and tool declarations
│   │   │   │   ├── dto/           # Custom tool request/response structures
│   │   │   │   ├── exception/     # MCP-specific errors
│   │   │   │   ├── response/      # Standard JSON RPC message formats
│   │   │   │   ├── security/      # Stdio thread-local security context
│   │   │   │   └── tools/         # Individual tool implementations (Weather, Journal, etc.)
│   │   │   ├── repository/        # MongoDB repositories
│   │   │   ├── scheduler/         # Automated spring scheduler tasks (e.g. email jobs)
│   │   │   └── service/           # Application service layers (Journal, User, Mail, etc.)
│   │   └── resources/
│   │       ├── application.yml    # Environment configurations & MCP setups
│   │       └── logback.xml        # Logback settings (redirects output for clean stdio transport)
├── pom.xml                        # Maven dependencies & build configurations
└── README.md                      # Project documentation
```

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

## 🔌 Model Context Protocol (MCP) Server Setup

This application includes a built-in **Model Context Protocol (MCP)** server, allowing AI assistants (like Claude Desktop, Cursor, Gemini Antigravity, etc.) to securely interact with your journal, perform sentiment analysis, check weather, send emails, and retrieve administrative stats.

### Architecture and stdio Transport
The MCP server operates over standard input/output (`stdio`). To prevent standard application logging and banner output from corrupting the JSON-RPC communication channel:
- **`System.out` Redirected to `System.err`**: The application redirects default standard out to standard error on startup.
- **Logback Configuration**: All logging appenders (including Console) target `System.err`.
- **Rolling Log Files**: Logs are written to `${user.home}/journalApp.log` instead of the root directory.
- **Spring Boot Banner**: The Spring Boot startup banner is disabled (`banner-mode: "off"`).

### Configuration Options
You can configure MCP settings in `src/main/resources/application.yml`:
```yaml
mcp:
  server:
    name: "Journal App MCP Server"
    version: "1.0.0"
    transport: "stdio"
  security:
    default-user: "admin" # Default fallback user for tools
```

### Available Tools (12 Tools)

The MCP server exposes the following tools:

| Category | Tool Name | Description |
| :--- | :--- | :--- |
| **Journaling** | `create_journal_entry` | Create a new journal entry for the user. |
| | `get_my_journal_entries` | Retrieve all journal entries of the user. |
| | `get_journal_by_id` | Retrieve a specific journal entry by ID. |
| | `update_journal` | Update an existing journal entry's title/content. |
| | `delete_journal` | Delete a specific journal entry by ID. |
| | `search_journal` | Search journal entries by title, tags, or sentiment. |
| **Sentiment** | `analyze_sentiment` | Perform sentiment analysis on arbitrary text. |
| **Weather** | `todays_weather` | Retrieve the weather forecast for a given city. |
| **Email** | `send_email` | Send an email to a recipient address. |
| **User Profile**| `get_user_profile` | Retrieve current user profile settings. |
| | `update_user_profile` | Update email, password, or sentiment analysis options. |
| **Admin** | `admin_get_all_users` | Retrieve all users in the system (requires ADMIN). |
| | `admin_delete_user` | Delete a specific user by username (requires ADMIN). |
| | `admin_get_statistics` | Retrieve dashboard stats (requires ADMIN). |

### Programmatic Integration with AI Agents (e.g., Claude Desktop)

To use the Journal App MCP Server with Claude Desktop, add the following configuration under `mcpServers` in your `claude_desktop_config.json` (typically located in `%APPDATA%\Claude\claude_desktop_config.json` on Windows):

```json
{
  "mcpServers": {
    "journal-app-mcp": {
      "command": "java",
      "args": [
        "-jar",
        "C:/Users/phapa/OneDrive/Desktop/journalApplication/target/journalApp-0.0.1-SNAPSHOT.jar"
      ],
      "env": {
        "MONGODB_URI": "mongodb://localhost:27017/journaldb",
        "REDIS_HOST": "localhost",
        "WEATHER_API_KEY": "your_api_key_here",
        "JAVA_EMAIL": "your_email@gmail.com",
        "JAVA_EMAIL_PASSWORD": "your_app_password"
      }
    }
  }
}
```
*(Make sure to build the jar using `./mvnw.cmd clean package` first).*

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
