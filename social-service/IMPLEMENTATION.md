# Social Service Implementation Details

The Social Service manages user relationships, friend requests, and social groups.

## 1. API Reference

### Friend Management
| Method | Endpoint | Parameters | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/social/friends/request` | `requesterId`, `targetUserId` | Sends a friend request. |
| `PUT` | `/api/social/friends/{requestId}/respond` | `status` (Query) | Responds to a request (ACCEPTED/REJECTED). |
| `GET` | `/api/social/friends/{userId}/list` | - | Returns a list of friend IDs. |
| `GET` | `/api/social/friends/{userId}/pending` | - | Returns pending friend requests. |

### Group Management
| Method | Endpoint | Parameters | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/social/groups` | `name`, `description`, `createdBy` | Creates a new social group. |
| `POST` | `/api/social/groups/{groupId}/members` | `userId` | Adds a member to a group. |
| `GET` | `/api/social/groups/{groupId}/members` | - | Lists group members. |
| `GET` | `/api/social/groups/user/{userId}` | - | Lists groups a user belongs to. |

## 2. Core Logic

### Friendship Statuses
- **PENDING**: Request sent but not yet acted upon.
- **ACCEPTED**: Request accepted; users are now friends.
- **REJECTED**: Request denied.

### Group Roles
- **ADMIN**: The creator of the group; has full control.
- **MEMBER**: Regular participant in the group.

## 3. Key Components
- **FriendshipRepository**: Manages friendship state in MySQL.
- **SocialGroupRepository / GroupMemberRepository**: Manages group entities and member associations.
