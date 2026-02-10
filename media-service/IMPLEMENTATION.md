# Media Service Implementation Details

The Media Service provides a unified, cross-service API for storing and retrieving media assets.

## 1. API Reference

| Method | Endpoint | Parameters | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/media/upload` | `file`, `folder` | Uploads a file to the active storage provider. |
| `DELETE` | `/api/media/delete` | `fileUrl` | Deletes a file from the storage provider. |
| `GET` | `/api/media/files/{folder}/{name}` | - | Serves a locally stored file (Local Provider only). |

## 2. Core Logic

### Cloud Agnostic Abstraction
The service uses the `StorageService` interface to hide the implementation details of different cloud providers.

### Supported Providers
- **Local Provider**: Directly uses the filesystem. Best for development.
- **S3 Provider**: Uses the AWS SDK for Java 2.x to interact with S3 buckets.
- **Azure Provider**: Uses the Azure SDK for Java to interact with Blob Storage.

### Config-Driven Activation
Uses `@ConditionalOnProperty` to instantiate the correct storage bean based on the `storage.provider` property in `application.properties`.

## 3. Flow: File Upload
1. `MediaController` receives a `MultipartFile` and a `folder` name.
2. The active `StorageService` implementation is called.
3. A unique name is generated for the file (UUID + original name).
4. The file is streamed to the target endpoint (local disk, S3, or Azure).
5. A URL pointing to the resource is returned.

## 4. Key Components
- **StorageConfig**: Conditional bean definitions for AWS and Azure clients.
- **LocalStorageService**: Manages directories and file IO for local storage.
- **S3StorageService / AzureBlobStorageService**: Cloud-specific client integrations.
