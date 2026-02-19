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

| Service | Port | Description |
| :--- | :--- | :--- |
| **API Gateway** | 8080 | Central Entry Point |
| **Discovery Service** | 8761 | Eureka Dashboard |
| **Auth Service** | 8081 | Identity & Auth |
| **Feed Service** | 8082 | Location & Social Feed |
| **Media Service** | 8083 | Multi-Cloud Media Storage |
| **News Service** | 8084 | Localized News Aggregator |
| **Social Service** | 8086 | Relationships & Groups |
| **Kibana** | 5601 | Log Analysis & APM |

### 5. Documentation

The project includes detailed documentation for each layer:

- **Architecture**: See [WALKTHROUGH.md](./WALKTHROUGH.md) for request flows.
- **Frontend Integration**: See [UI_INTEGRATION.md](./UI_INTEGRATION.md) for API contracts.
- **Security**: See [JWT_FLOW.md](./JWT_FLOW.md).
- **Operations**: See [PRODUCTION_READINESS.md](./PRODUCTION_READINESS.md).

### 6. Stopping the System
To stop and remove the containers, use:
```bash
docker-compose down
```

---
*For a detailed log of recent changes and architectural flow, see [WALKTHROUGH.md](./WALKTHROUGH.md).*
