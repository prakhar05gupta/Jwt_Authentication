# JWT Authentication System

A full-stack authentication system built with **Spring Boot 4.x + Java 21** backend and a vanilla HTML/CSS/JS frontend.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.x |
| Security | Spring Security + JWT |
| Password Storage | BCrypt (strength 8) |
| Database | MySQL |
| ORM | Hibernate / Spring Data JPA |
| Frontend | HTML5 + CSS3 + Vanilla JS |

---

## Deliverables

### ✅ Auth Flow Working End-to-End
- User registers → JWT token generated → returned to client
- User logs in → credentials validated → JWT token returned
- Protected routes reject requests without valid token (401)

### ✅ Token Handling in Frontend
- JWT token stored in `localStorage` after login/register
- Token automatically attached to every protected API call via `Authorization: Bearer <token>` header
- Auto-login on page refresh if valid token exists in localStorage
- Token cleared on logout

### ✅ Secure Password Storage
- Passwords hashed using **BCrypt** with strength factor 8
- Plain text passwords are **never stored** in the database
- BCrypt generates a unique salt for every password automatically

---

## Project Structure

```
jwt-auth-system/
├── user_service/          ← Spring Boot Backend
│   └── src/main/java/
│       ├── config/
│       │   ├── SecurityConfig.java         # Spring Security config
│       │   ├── JwtAuthenticationFilter.java # JWT filter chain
│       │   ├── CorsConfig.java             # CORS for frontend
│       │   └── ModelMapperConfig.java
│       ├── controller/
│       │   ├── AuthController.java         # /api/auth/register, /login
│       │   └── UserController.java         # /api/users/profile (protected)
│       ├── service/
│       │   ├── JwtService.java             # Token generate/validate
│       │   ├── UserService.java
│       │   └── UserServiceImpl.java
│       ├── entity/
│       │   └── User.java                   # Users table with indexes
│       ├── dto/
│       │   ├── RegisterRequestDto.java
│       │   ├── LoginRequestDto.java
│       │   └── AuthResponseDto.java        # Returns token + user info
│       └── exception/
│           └── GlobalExceptionHandler.java # Centralized error handling
│
└── frontend/
    └── index.html         ← Complete frontend (single file)
```

---

## How to Run

### Step 1 — Setup MySQL
```sql
CREATE DATABASE user_db;
```

### Step 2 — Configure application.yml
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_db
    username: your_mysql_username
    password: your_mysql_password
```

### Step 3 — Run Spring Boot Backend
```bash
cd user_service
mvn spring-boot:run
```
Backend runs on: `http://localhost:8081`

### Step 4 — Open Frontend
Open `frontend/index.html` using **VS Code Live Server** (port 5500)
or any local server.

---

## API Endpoints

### Public (No token required)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and get JWT token |

### Protected (Requires `Authorization: Bearer <token>`)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/users/profile` | Get current user profile | Authenticated |
| GET | `/api/users/{id}` | Get user by ID | Authenticated |
| GET | `/api/users/all` | Get all users | ADMIN only |
| PUT | `/api/users/{id}` | Update user | Own profile or ADMIN |
| DELETE | `/api/users/{id}` | Delete user | ADMIN only |

---

## JWT Flow

```
1. User sends credentials (POST /api/auth/login)
         ↓
2. Server validates credentials against DB
         ↓
3. Server generates JWT (contains username + role, expires in 24h)
         ↓
4. JWT returned to client in response body
         ↓
5. Client stores JWT in localStorage
         ↓
6. Client sends JWT in Authorization header for every protected request
         ↓
7. JwtAuthenticationFilter validates token on every request
         ↓
8. If valid → request reaches controller
   If invalid/expired → 401 Unauthorized
```

---

## Security Features

- JWT tokens signed with HS256 algorithm
- BCrypt password hashing — passwords never stored in plain text
- Stateless session management — no server-side session storage
- Role-Based Access Control (ADMIN / MANAGER / MEMBER)
- Global Exception Handler — no sensitive info leaked in errors
- DB indexes on `username` and `email` for optimized queries

---

## Performance

| Metric | Value |
|---|---|
| Registration time (before optimization) | ~220ms |
| Registration time (after optimization) | ~100ms |
| Improvement | **55% faster** |

Optimizations: query reduction, DB indexing, BCrypt strength tuning, HikariCP connection pooling
