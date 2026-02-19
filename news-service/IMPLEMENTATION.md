# News Service Implementation Details

The News Service aggregates and serves localized news content to users.

## 1. API Reference

### Local News
| Method | Endpoint | Parameters | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/news/local-feed` | `pincode`, `taluk`, `district`, `state` | Fetches news based on location hierarchy. |

### News Source Management
| Method | Endpoint | Body | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/news/sources` | - | Lists all news sources. |
| `POST` | `/api/news/sources` | `NewsSource` | Adds a new news source and its RSS/API URL. |
| `POST` | `/api/news/sources/bulk` | `List<NewsSource>` | Bulk adds multiple sources (duplicates auto-filtered). |

## 2. Core Logic

### Generalized Location Fetching
The News Service attempts to find news at the most granular level provided:
1. Pincode
2. Taluk
3. District
4. State

### Source Deduplication
When adding news sources, the service checks the `URL` to ensure no duplicate sources are registered.

## 3. Key Components
- **NewsService**: Interfaces with external news providers/RSS feeds.
- **NewsSourceRepository**: Stores metadata about news publishers and their regions.
