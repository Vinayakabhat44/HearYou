---
description: How to start the Mitra (Antigravity) Application
---

### 1. Start Backend Services
Ensure you have Docker Desktop running, then start the microservices:
```powershell
# From the project root
docker-compose up -d
```
> [!NOTE]
> This will start all services (Auth, Social, Feed, News, Media, Gateway, Discovery, Redis, Postgres).

### 2. Start Frontend UI
Open a new terminal and start the Vite development server:
```powershell
cd antigravity-ui
npm run dev
```

### 3. Access the Application
Once the dev server is running, open your browser to:
[http://localhost:5173](http://localhost:5173)

---
// turbo-all
