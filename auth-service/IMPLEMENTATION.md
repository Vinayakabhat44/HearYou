# Auth Service Implementation Details

The Auth Service manages user accounts, authentication, and security context.

## 1. API Reference

| Method | Endpoint | Request Body | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | `RegisterRequest` | Registers a new user with optional location details. |
| `POST` | `/api/auth/login` | `AuthRequest` | Authenticates user and returns a JWT. |
| `PUT` | `/api/users/{id}/location` | `lat`, `lng` (Query) | Updates user's home location and performs reverse geocoding. |
| `GET` | `/api/users/{id}` | - | Retrieves user profile data. |

## 2. Core Logic

### Registration & Analytics
- **Password Hashing**: Uses `BCryptPasswordEncoder`.
- **Automatic Geocoding**: If a user registers with coordinates but no address, the service automatically calls `OpenStreetMapService` to populate `village`, `taluk`, `district`, and `state`.
- **Uniqueness**: Enforces unique `username` and `email`.

### Security Implementation
- **JWT Issuing**: Signs tokens using a private key.
- **CustomUserDetailsService**: Integration with Spring Security for loading user entities.

## 3. Flow: User Registration
1. `AuthController` receives `RegisterRequest`.
2. `AuthServiceImpl` checks for existing users.
3. Encodes password.
4. If coordinates provided:
   - Sets JTS `Point`.
   - Fetches missing address details via `GeocodingService`.
5. Saves `User` entity to MySQL.
6. Generates and returns a JWT.

## 4. Key Components
- **UserRepository**: MySQL JPA repository with spatial support.
- **JwtUtil**: Handles token generation and key management.
- **OpenStreetMapService**: Communicates with Nominatim API for reverse geocoding.
