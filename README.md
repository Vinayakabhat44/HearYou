# MitraAI — Location-Based Social Platform

MitraAI is a location-aware social and news platform built on a macroservice architecture using Spring Boot, Spring Cloud, MySQL, Redis, and the Elastic Stack.

## 🏗️ Architecture Overview

The application uses **two core backend services** behind a unified API Gateway:

| Service | Role |
| :--- | :--- |
| **mitra-auth-service** | Authentication, User Management, Preferences |
| **mitra-core-service** | Feed, Social, News, and Media (consolidated) |
| **mitra-gateway-service** | Unified API entry point (port 8080) |
| **mitra-discovery-service** | Eureka service registry |

---

## 🚀 Getting Started

### Prerequisites
- [Docker Desktop](https://www.docker.com/get-started) installed and **running**.
- Java 17 + Maven (for local development).

### Configuration
The system uses a `.env` file for secrets. Review it before launching:
```
DB_ROOT_PASSWORD=...
JWT_KEYSTORE_PASSWORD=mitraai123
NEWS_API_KEY=...
```
> The `.env` file is gitignored. Do not commit secrets.

### Build & Launch
```bash
docker-compose up --build
```

### Verification

| Service | URL | Description |
| :--- | :--- | :--- |
| **API Gateway** | http://localhost:8080 | Central entry point |
| **Eureka Dashboard** | http://localhost:8761 | Service registry |
| **Auth Service** | http://localhost:8081 | Identity & Auth |
| **Core Service** | http://localhost:8082 | Feed, Social, News, Media |
| **Kibana** | http://localhost:5601 | Logs & APM |

### Stopping
```bash
docker-compose down
```

---

## 📚 Documentation

| Document | Purpose |
| :--- | :--- |
| [WALKTHROUGH.md](./WALKTHROUGH.md) | Architecture & request flow diagrams |
| [UI_INTEGRATION.md](./UI_INTEGRATION.md) | API contracts for frontend integration |
| [JWT_FLOW.md](./JWT_FLOW.md) | Stateless JWT auth architecture |
| [PRODUCTION_READINESS.md](./PRODUCTION_READINESS.md) | Operations & security readiness report |
