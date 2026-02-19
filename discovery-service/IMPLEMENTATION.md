# Discovery Service Implementation Details

The Discovery Service provides a central registry for all microservices using Netflix Eureka.

## 1. Configuration

### Eureka Server
- **Port**: 8761
- **Technology**: Spring Cloud Netflix Eureka Server.
- **Role**: Allows services to register themselves and discover others without hardcoded hostnames or IPs.

## 2. Key Features
- **Self-Preservation**: Prevents mass deregistration during network partitions.
- **Dashboard**: Provides a visual overview of all up/down services at `http://localhost:8761`.

## 3. Deployment Notes
- **Docker**: Configured to wait for healthy infrastructure (Elasticsearch, Logstash) before starting.
- **Environment**: Uses `EUREKA_HOSTNAME` for consistent naming in the Docker network.
