# Design Document: Location-Based Feed Application

## 1. System Overview
The application is a location-based social feed platform for Android and iOS. Users can share stories (Audio, Video, Text) and consume content relevant to their current location. It features a **Hierarchical Feed System**, prioritizing content from the user's immediate Village/Taluk up to District and State levels.

### High-Level Requirements
- **Platforms**: Android & iOS (React Native)
- **Backend**: Java Spring Boot
- **Database**: **MySQL 8.0+** (Selected for Spatial support + Relational stability)
- **Location Strategy**: Hybrid (Hierarchical Administrative Regions + Geospatial Proximity)
- **Storage**: AWS S3 / MinIO

## 2. Architecture Diagram

```mermaid
graph TD
    UserApp["Mobile App (React Native)"] --> Gateway["API Gateway (Port 8080)"]
    Gateway --> Discovery["Discovery Service (Eureka)"]
    Gateway --> Auth_Service["Auth Service"]
    Gateway --> Feed_Service["Feed Service"]
    Gateway --> Media_Service["Media Service"]
    
    Feed_Service --> DB[("MySQL Database")]
    Feed_Service --> ObjectStore["Object Storage"]
    
    Note1["Auth Service issues JWT signed with Private Key"]
    Note2["Services validate JWT using Public Key"]
```

## 3. Request Flow: Hierarchical Feed
When a client hits `GET http://localhost:8080/api/feed/hierarchical`, the flow is as follows:

1.  **API Gateway (`gateway-service`)**:
    *   Receives the request on port 8080.
    *   Identifies the route `/api/feed/**` and resolves `FEED-SERVICE` via **Eureka**.
    *   Forwards the request (with the `Authorization` header) to an available instance of `feed-service`.
2.  **Discovery Service (`discovery-service`)**:
    *   Acts as a service registry (Eureka).
    *   Provides the dynamic IP/Port of `feed-service` to the Gateway.
3.  **Feed Service (`feed-service`)**:
    *   **JwtAuthenticationFilter**: Intercepts the request and extracts the JWT.
    *   **JwtUtil**: Validates the JWT using a local **Public Key** (no synchronous call to Auth Service).
    *   **Security Context**: Sets the `userId` extracted from token claims.
    *   **FeedController**: Calls `storyService.getHierarchicalFeed(userId)`.
4.  **Auth Service (`auth-service`)**:
    *   Not directly called in the request flow (Authentication is stateless via JWT).
    *   Responsible for initial token issuance during Login/Signup.

## 4. Backend Design (Spring Boot)
### 4.1 Technologies
- **Framework**: Spring Boot 3.x
- **Database**: MySQL 8.x
- **Maps/Geocoding**: Google Maps API or OpenStreetMap (Nominatim) for Reverse Geocoding.

### 4.2 Key Logic: Hierarchical Feed
The feed will fetch content in priority buckets:
1.  **Village/Town Level**: Exact match on `village_name`.
2.  **Taluk/Sub-District Level**: Match `taluk_name`, exclude already seen.
3.  **District Level**: Match `district_name`.
4.  **State/Country**: Trending items in the region.

### 4.3 Database Schema (MySQL)
**Users Table**
- `id`, `username`, `email`
- `home_location` (POINT) - Exact coordinate
- `village` (VARCHAR), `taluk` (VARCHAR), `district` (VARCHAR), `state` (VARCHAR) - *Auto-filled via Geocoding, editable by user.*

**Stories Table**
- `id`, `user_id`, `type`
- `location` (POINT)
- `village`, `taluk`, `district`, `state` - *Captured at time of upload.*

## 5. Frontend Design (React Native)
### 4.1 Signup Flow Changes
- **Location Permission Request**.
- **Auto-Detect**: Fetch Lat/Lng -> Call API to populate Village/Taluk/etc.
- **Manual Confirmation**: User confirms or edits their "Home Location" details.

## 6. API Endpoints (Refined)
### Stories
- `GET /api/feeds/hierarchical?userId=...`
    - Backend logic determines the mix of Village vs District content.

### Metadata / Utilities
- `GET /api/location/reverse-geocode?lat=...&lng=...`
    - Returns: `{ village: "...", taluk: "...", district: "..." }`

## User Review Required
> [!IMPORTANT]
> **Geocoding API Costs**: Reverse geocoding (converting Lat/Lng to Village Name) on every post/signup can incur costs (Google Maps) or rate limits (OpenStreetMap). We should implement **Caching** or use a self-hosted Geocoder if volume is high.
