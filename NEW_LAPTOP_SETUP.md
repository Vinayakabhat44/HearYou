# 🚀 Mitra (Antigravity) — New Laptop Setup Guide

> A complete guide to setting up the Mitra application on a fresh Windows machine.

---

## 📦 Required Software

| # | Software | Why Needed | Version |
|---|----------|-----------|---------|
| 1 | **Java JDK 17** | Build & run Spring Boot microservices | JDK **17** (exact) |
| 2 | **Maven** | Build tool for all Java services | 3.8+ |
| 3 | **Node.js** | Run the React/Vite frontend (`antigravity-ui`) | v18+ LTS |
| 4 | **npm** | Frontend package manager | Bundled with Node.js |
| 5 | **Docker Desktop** | Runs all backend infrastructure + microservices | Latest stable |
| 6 | **Git** | Clone the repository | Latest |

> **Note:** MySQL 8, Redis, Elasticsearch 8.12, Kibana, APM Server, Logstash, and all Spring Boot services run **inside Docker** — no separate installation needed for those.

---

## ⬇️ Download Links

| Software | Download URL |
|----------|-------------|
| Java JDK 17 | https://adoptium.net/ (Eclipse Temurin 17) |
| Maven | https://maven.apache.org/download.cgi |
| Node.js | https://nodejs.org/en (LTS version) |
| Docker Desktop | https://www.docker.com/products/docker-desktop/ |
| Git | https://git-scm.com/download/win |

---

## ✅ Validation Commands

Run these in **PowerShell** after installing each tool to confirm correct setup.

### 1. Java JDK 17
```powershell
java -version
# Expected: openjdk version "17.x.x" ...

javac -version
# Expected: javac 17.x.x
```

### 2. Maven
```powershell
mvn -version
# Expected: Apache Maven 3.x.x ... Java version: 17
```

### 3. Node.js & npm
```powershell
node -version
# Expected: v18.x.x or v20.x.x

npm -version
# Expected: 9.x.x or 10.x.x
```

### 4. Docker Desktop
```powershell
docker -version
# Expected: Docker version 27.x.x, build ...

docker compose version
# Expected: Docker Compose version v2.x.x

# Verify Docker daemon is running
docker ps
# Expected: empty container table (no errors)
```

### 5. Git
```powershell
git -version
# Expected: git version 2.x.x.windows.x
```

### 6. Java Home (Critical)
```powershell
echo $env:JAVA_HOME
# Expected: C:\Program Files\Eclipse Adoptium\jdk-17.x.x.x-hotspot (or similar)
# If empty, Maven builds will fail — set JAVA_HOME manually in System Environment Variables
```

---

## 🔧 Project Setup Steps

### Step 1: Clone the Repository
```powershell
git clone <your-repo-url>
cd Antigravity
```

### Step 2: Create the `.env` File
The `.env` file is **not committed to Git**. Create it manually at the project root:

```env
# MySQL Configuration
DB_ROOT_PASSWORD=Password
DB_USERNAME=root

# Service Database URLs
DB_AUTH_URL=jdbc:mysql://mysql:3306/mitra_auth?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false
DB_CORE_URL=jdbc:mysql://mysql:3306/mitra_core?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false

# Secrets
JWT_KEYSTORE_PASSWORD=mitraai123

# Discovery
EUREKA_SERVER=http://mitra-discovery-service:8761/eureka/

# Infrastructure
REDIS_HOST=redis
REDIS_PORT=6379

# API Keys
NEWS_API_KEY=<your_news_api_key>

# Elastic Stack
ELASTIC_VERSION=8.12.0
ELASTIC_PASSWORD=mitraai_elk_pwd
KIBANA_PASSWORD=mitraai_kibana_pwd
ELASTIC_APM_SECRET_TOKEN=mitraai_apm_token
ELASTIC_APM_SERVER_URL=http://apm-server:8200
```

### Step 3: Start Docker Desktop
Make sure Docker Desktop is fully running (check the system tray icon shows it's running).

### Step 4: Start All Backend Services
```powershell
# From the project root (d:\Vinayak\Personal_Project\Antigravity)
docker compose up -d
```

This spins up:
- `mitra-discovery-service` (Eureka) → port **8761**
- `mitra-gateway-service` → port **8080**
- `mitra-auth-service` → port **8081**
- `mitra-core-service` → port **8082**
- `mysql` → port **3307**
- `redis` → port **6380**
- `elasticsearch` → port **9200**
- `kibana` → port **5601**
- `apm-server` → port **8200**
- `logstash` → port **5044**

### Step 5: Setup & Start Frontend
```powershell
cd antigravity-ui
npm install
npm run dev
```

### Step 6: Open the App
```
http://localhost:5173
```

---

## 🔍 Verify Services Are Up

```powershell
# Check all containers are running
docker compose ps

# Check Eureka dashboard
start http://localhost:8761

# Check Gateway health
curl http://localhost:8080/actuator/health

# Check Auth Service health
curl http://localhost:8081/actuator/health

# Check Core Service health
curl http://localhost:8082/actuator/health

# Check Kibana
start http://localhost:5601
```

---

## ⚠️ Important Notes

1. **Docker RAM**: Elasticsearch needs ~2 GB alone. Set Docker Desktop to use **at least 6–8 GB RAM**.
   - Docker Desktop → Settings → Resources → Memory → 8 GB

2. **JAVA_HOME**: Must be set as a System Environment Variable pointing to JDK 17, or Maven will fail.

3. **First Docker build**: The first `docker compose up` will take **10–15 minutes** as it downloads base images and builds all services.

4. **Service startup order**: Services start in order (Elasticsearch → Kibana → APM Server → Logstash → Discovery → Gateway → Auth → Core). Wait ~3–5 minutes after `docker compose up -d` before testing APIs.

5. **`.env` file**: Never commit this file to Git. It contains passwords and API keys.

---

## 🗂️ Project Structure Overview

```
Antigravity/
├── antigravity-ui/          # React + Vite frontend
├── mitra-auth-service/      # Spring Boot - Authentication
├── mitra-core-service/      # Spring Boot - Core (Feed, News, Stories)
├── mitra-discovery-service/ # Spring Boot - Eureka Service Registry
├── mitra-gateway-service/   # Spring Boot - API Gateway
├── logstash/                # Logstash pipeline config
├── docker-compose.yml       # Full stack orchestration
├── .env                     # Environment variables (create manually)
├── mysql-init.sql           # DB initialization script
└── pom.xml                  # Maven parent POM
```

---

*Last updated: April 2026*
