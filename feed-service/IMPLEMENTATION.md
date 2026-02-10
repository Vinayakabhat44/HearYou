# Feed Service Implementation Details

The Feed Service is the core logic engine for processing stories and generating location-aware social feeds.

## 1. API Reference

| Method | Endpoint | Parameters | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/stories` | `story`, `file`, `lat`, `lng` | Creates a new story with media and location data. |
| `GET` | `/api/stories/{id}` | - | Retrieves details of a specific story. |
| `DELETE` | `/api/stories/{id}` | - | Deletes a story (Authorized for creator only). |
| `GET` | `/api/feed/hierarchical` | - | Returns a hierarchical feed based on user's location. |

## 2. Core Logic

### Hierarchical Feed Calculation
Stories are retrieved based on a geographic hierarchy to ensure users see relevant local content first:
1. **Village Level**: Highest priority.
2. **Taluk Level**: Secondary priority.
3. **District Level**: Tertiary priority.

### Redis Location Caching
To avoid expensive inter-service calls to the `Auth Service`, the user's location data is cached in Redis:
- **Cache Key**: `userLocation:{userId}`
- **TTL**: Configured for efficient reuse across requests.
- **Service**: `LocationCacheService.java`.

## 3. Spring AOP Concepts
The Feed Service extensively uses AOP to keep business logic clean:

- **Performance Monitoring**: `@TrackTime` annotation triggers `LoggingAspect` to measure method duration.
- **Custom Security**: `@AuthorizeOwner` annotation triggers `SecurityAspect` to verify that the `userId` in the JWT matches the story owner.
- **Audit Logging**: `@AuditLog` annotation triggers `AuditAspect` to record critical business actions.
- **Robustness**: `@Retry` annotation triggers `RetryAspect` to handle transient failures in remote Feign calls.
- **Error Tracking**: `ExceptionLoggingAspect` captures and logs all service-layer exceptions automatically.

## 4. Flow: Story Creation
1. `StoryController` receives `multipart/form-data`.
2. `StoryServiceImpl.createStory` is called (AOP: Entry Logging, TrackTime).
3. If media is attached, calls `MediaServiceClient` (Feign) to upload to Media Service.
4. Handles location:
   - Uses provided `lat`/`lng` OR fetches from Redis/Auth Service.
   - Performs reverse geocoding via `GeocodingService`.
5. Saves story to MySQL with spatial JTS `Point`.
6. Returns saved story (AOP: Exit Logging, Audit Log).

## 5. Key Components
- **StoryRepository**: Custom Native SQL queries for hierarchical prioritization.
- **MediaServiceClient**: Feign client for inter-service communication.
- **SecurityContextHolder**: Used by AOP aspects to identify the current user.
