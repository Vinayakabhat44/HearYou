# UI Integration Guide: Antigravity

**Date:** 2026-02-14
**Version:** 1.0

This document outlines the API endpoints, authentication data flow, and resource structures required for frontend integration.

## 1. Global Configuration

*   **Base URL:** `http://localhost:8080` (Gateway)
*   **Authentication:** Bearer Token (JWT) required in `Authorization` header for all protected endpoints.
    *   Header: `Authorization: Bearer <token>`
*   **Content-Type:** `application/json` (unless specifying `multipart/form-data` for uploads).

---

## 2. Authentication & User Profile (`auth-service`)

### Login
*   **Endpoint:** `POST /api/auth/login`
*   **Body:**
    ```json
    {
      "username": "user1",
      "password": "password"
    }
    ```
*   **Response:**
    ```json
    {
      "token": "eyJhbG...",
      "username": "user1",
      "email": "user1@example.com",
      "preferences": { "theme": "dark", "lang": "en" }
    }
    ```

### Register
*   **Endpoint:** `POST /api/auth/register`
*   **Body:**
    ```json
    {
      "username": "user1",
      "password": "password",
      "email": "user1@example.com",
      "mobileNumber": "1234567890",
      "latitude": 12.9716,
      "longitude": 77.5946
    }
    ```

### User Preferences
*   **Get Preferences:** `GET /api/users/{id}/preferences`
*   **Update Preferences:** `PUT /api/users/{id}/preferences`
    *   **Body:** `{"preferences": {"theme": "dark", "notifications": "enabled"}}`

### User Profile
*   **Get Profile:** `GET /api/users/{id}`
*   **Search Users:** `GET /api/users/search?query=vinayak`
*   **Update Location:** `PUT /api/users/{id}/location?lat=12.9&lng=77.6`

---

## 3. Social Interactions (`social-service`)

### Friend Management
*   **List Friends:** `GET /api/social/friends/{userId}/list`
    *   Returns: `[101, 102, 105]` (List of Friend User IDs)
*   **Pending Requests:** `GET /api/social/friends/{userId}/pending`
*   **Send Request:** `POST /api/social/friends/request?requesterId={id}`
    *   **Body:** `{"targetUserId": 102}`
*   **Respond to Request:** `PUT /api/social/friends/{requestId}/respond?status=ACCEPTED`
    *   Status options: `ACCEPTED`, `REJECTED`

---

## 4. Content Feeds (`feed-service`)

### Feeds
*   **Main Feed (Hierarchical):** `GET /api/feed/hierarchical`
    *   Logic: Prioritizes local content based on User's location (District -> State -> National).
*   **Friends Feed:** `GET /api/feed/friends`
    *   Logic: Shows stories from connected friends.

---

## 5. News & Information (`news-service`)

### Local News
*   **Get News:** `GET /api/news/local-feed`
*   **Params (Optional):** `?pincode=560001&district=Bangalore`
    *   If params are omitted, logic currently defaults or requires implementation to fetch from user profile context if implicit.

---

## 6. Media Management (`media-service`)

### File Operations
*   **Upload:** `POST /api/media/upload` (Multipart)
    *   Params: `file` (Binary), `folder` (String, e.g., "avatars", "posts")
    *   Returns: File URL/Path string.
*   **View/Download:** `GET /api/media/files/{folder}/{fileName}`
