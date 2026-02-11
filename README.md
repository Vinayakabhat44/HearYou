# Antigravity Microservices

Antigravity is a location-based social feed microservices ecosystem built with Spring Boot, MySQL, Redis, and Eureka.

## 🚀 Getting Started

Follow these steps to bring the entire system up using Docker.

### 1. Prerequisites
- [Docker](https://www.docker.com/get-started) installed on your machine.
- [Docker Compose](https://docs.docker.com/compose/install/) (included with Docker Desktop).

### 2. Configuration
The system uses a `.env` file for secrets and infrastructure configuration. 
- Open the [`.env`](./.env) file in the root directory.
- Review the default passwords and settings.
- **Note**: The `.env` file is gitignored for security.

### 3. Build and Launch
Run the following command from the root directory to build the images and start the containers:

```bash
docker-compose up --build
```

### 4. Verification

Once the services are up, you can verify them at these locations:

| Service | Port | Health Check / Dashboard |
| :--- | :--- | :--- |
| **API Gateway** | 8080 | [Health Check](http://localhost:8080/actuator/health) |
| **Discovery Service** | 8761 | [Eureka Dashboard](http://localhost:8761) |
| **Auth Service** | 8081 | [Health Check](http://localhost:8081/actuator/health) |
| **Feed Service** | 8082 | [Health Check](http://localhost:8082/actuator/health) |
| **Media Service** | 8083 | [Health Check](http://localhost:8083/actuator/health) |

### 5. Stopping the System
To stop and remove the containers, use:
```bash
docker-compose down
```

---
*For a detailed log of recent changes and architectural flow, see [WALKTHROUGH.md](./WALKTHROUGH.md).*
