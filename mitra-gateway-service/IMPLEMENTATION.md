# Gateway Service Implementation Details

The Gateway Service is the single entry point for all client requests, providing routing and security.

## 1. Routing Rules

The gateway routes requests to downstream services based on the path prefix:

| Path Pattern | Target Service |
| :--- | :--- |
| `/api/auth/**` | `AUTH-SERVICE` |
| `/api/users/**` | `AUTH-SERVICE` |
| `/api/stories/**` | `FEED-SERVICE` |
| `/api/feed/**` | `FEED-SERVICE` |
| `/api/media/**` | `MEDIA-SERVICE` |
| `/api/news/**` | `NEWS-SERVICE` |
| `/api/social/**` | `SOCIAL-SERVICE` |

## 2. Key Features
- **Dynamic Routing**: Uses Eureka service IDs (`lb://SERVICE-ID`) for load balancing across instances.
- **Header Preservation**: Forwards JWT and other headers to downstream services.
- **CORS Configuration**: Configured to allow requests from the React frontend.

## 3. Key Components
- **RouteLocator**: Defines the predicate and URI mapping.
- **GatewayDiscoveryClientAutoConfiguration**: Enables auto-discovery of routes from Eureka.
