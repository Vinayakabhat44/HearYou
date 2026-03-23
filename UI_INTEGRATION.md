# UI Integration Guide: MitraAI

**Version:** 2.0 — Macroservice Architecture
**Base URL:** `http://localhost:8080` (API Gateway)
**Auth:** `Authorization: Bearer <JWT>` header required on all protected endpoints.

---

## 1. Authentication (`mitra-auth-service`)

### Login
```
POST /api/auth/login
```
```json
{ "username": "user1", "password": "password" }
```
**Response:**
```json
{ "token": "eyJhbG...", "username": "user1", "email": "user1@example.com" }
```

### Register
```
POST /api/auth/register
```
```json
{
  "username": "user1", "password": "password",
  "email": "user1@example.com", "mobileNumber": "1234567890",
  "latitude": 12.9716, "longitude": 77.5946
}
```

### User Management
| Action | Endpoint |
| :--- | :--- |
| Get Profile | `GET /api/users/{id}` |
| Search Users | `GET /api/users/search?query=name` |
| Update Location | `PUT /api/users/{id}/location?lat=12.9&lng=77.6` |
| Get Preferences | `GET /api/users/{id}/preferences` |
| Update Preferences | `PUT /api/users/{id}/preferences` |

---

## 2. Feed (`mitra-core-service`)

| Action | Endpoint | Notes |
| :--- | :--- | :--- |
| Hierarchical Feed | `GET /api/feed/hierarchical` | Location-based: village → taluk → district |
| Friends Feed | `GET /api/feed/friends` | Stories from connected users |
| Create Story | `POST /api/feed/stories` (JSON) | Text-only story |
| Create Story with Media | `POST /api/feed/stories/upload` (Multipart) | With image/video |
| Get Story | `GET /api/feed/stories/{id}` | |
| Delete Story | `DELETE /api/feed/stories/{id}` | Owner only |

---

## 3. Social (`mitra-core-service`)

### Friend Management
| Action | Endpoint |
| :--- | :--- |
| List Friends | `GET /api/social/friends/{userId}/list` |
| Pending Requests | `GET /api/social/friends/{userId}/pending` |
| Send Request | `POST /api/social/friends/request?requesterId={id}` |
| Respond to Request | `PUT /api/social/friends/{requestId}/respond?status=ACCEPTED` |

### Group Management
| Action | Endpoint |
| :--- | :--- |
| Create Group | `POST /api/social/groups` |
| Add Member | `POST /api/social/groups/{groupId}/members` |
| List Groups | `GET /api/social/groups/user/{userId}` |

---

## 4. News (`mitra-core-service`)

| Action | Endpoint | Notes |
| :--- | :--- | :--- |
| Local News Feed | `GET /api/news/local-feed` | Params: `?pincode=&taluk=&district=&state=` |
| News by Keyword | `GET /api/news?keyword=cricket` | |
| List Sources | `GET /api/news/sources` | |
| Add RSS Sources | `POST /api/news/sources/bulk` | Admin |

**`LocalizedFeedResponse` shape:**
```json
{
  "userLocation": { "pincode": "560001", "taluk": "Bangalore North", "district": "Bangalore", "state": "Karnataka" },
  "feed": { "district": [...], "state": [...], "national": [...], "categories": { "cricket": [...] } }
}
```

---

## 5. Media (`mitra-core-service`)

| Action | Endpoint |
| :--- | :--- |
| Upload File | `POST /api/media/upload` (Multipart: `file`, `folder`) |
| View/Download | `GET /api/media/files/{folder}/{fileName}` |

---

## 6. Swagger / API Docs

| Service | Swagger UI |
| :--- | :--- |
| Auth Service | http://localhost:8081/swagger-ui.html |
| Core Service | http://localhost:8082/swagger-ui.html |
| Via Gateway | http://localhost:8080/swagger-ui.html |
