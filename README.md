# SyncSphere — Cloud Drive & File Storage Platform

SyncSphere (`Drive`) is a full-stack file storage and folder management web application built with **Spring Boot 3 (Java 21)**, **MySQL**, and **React 18**.

It supports user registration and authentication, file uploads/downloads/deletions, virtual folder creation and navigation, and clean REST API contracts.

---

## Features

- **User Accounts & Authentication**:
  - Salted SHA-256 password hashing (`PasswordHasher`) with safe `UserResponse` DTOs (passwords are never returned in API payloads).
  - Registration, login, profile lookup, profile updates, and account deletion.
- **File & Folder Management**:
  - Upload files up to 20 MB with path-traversal protection and UUID-prefixed disk storage.
  - Create virtual folders and browse nested directory levels.
  - Stream file downloads with `Content-Disposition: attachment` headers.
  - Delete files (and empty folders) from both disk and database.
- **Modular Full-Stack Architecture**:
  - **Backend**: Layered `controller`, `service`, `dto`, `exception`, `repo`, `entity`, `config`, and `util` packages with centralized `@RestControllerAdvice` error handling.
  - **Frontend**: Centralized Axios `apiClient`, `authService`, `fileService`, custom React hooks (`useAuth`, `useFiles`), and focused UI components.

---

## Project Structure

### Backend (`src/main/java/com/example/drive/`)

```text
src/main/java/com/example/drive/
├── DriveApplication.java                # Spring Boot entry point
├── config/
│   ├── GlobalCorsConfig.java            # Configurable CORS origins
│   └── StorageProperties.java           # @ConfigurationProperties(prefix = "file")
├── controller/
│   ├── FileController.java              # /api/files endpoints (upload, folders, list, download, delete)
│   └── UserController.java              # /api/users endpoints (register, login, profile CRUD)
├── dto/
│   ├── ApiErrorResponse.java            # Consistent JSON error payload
│   ├── CreateFolderRequest.java
│   ├── FileResponse.java
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   ├── UpdateUserRequest.java
│   └── UserResponse.java
├── entity/
│   ├── FileEntity.java                  # File & folder metadata entity
│   └── UserEntity.java                  # User account entity
├── exception/
│   ├── BadRequestException.java
│   ├── GlobalExceptionHandler.java      # @RestControllerAdvice exception mapper
│   ├── ResourceNotFoundException.java
│   └── UnauthorizedException.java
├── repo/
│   ├── FileRepository.java
│   └── UserRepository.java
├── service/
│   ├── FileStorageService.java          # Safe file I/O, folder validation & metadata management
│   └── UserService.java                 # Registration, login verification & user CRUD
└── util/
    └── PasswordHasher.java              # Salted SHA-256 password hashing & verification
```

### Frontend (`frontend/src/`)

```text
frontend/src/
├── App.js                               # Route definitions & auth guard
├── components/
│   ├── FileList.js                      # Folder creation, navigation, download & delete
│   ├── FileUpload.js                    # Multipart upload form with size validation
│   ├── Login.js
│   ├── Navbar.js
│   └── Register.js
├── hooks/
│   ├── useAuth.js                       # Session state & localStorage persistence
│   └── useFiles.js                      # Directory navigation & file operations state
├── services/
│   ├── apiClient.js                     # Shared Axios client & error extractor
│   ├── authService.js                   # Auth API & local storage helpers
│   └── fileService.js                   # File & folder API calls
└── utils/
    └── formatters.js                    # Human-readable file sizes & timestamps
```

---

## Tech Stack

- **Backend**: Java 21, Spring Boot 3.5.3, Spring Data JPA, MySQL 8, Maven
- **Frontend**: React 18, React Router v6, Axios, Create React App
- **Testing**: JUnit 5, Mockito, Spring Boot Test

---

## Getting Started

### 1. Prerequisites

- Java 21+
- Maven 3.8+ (or `./mvnw` / `mvnw.cmd`)
- Node.js 18+ and npm
- MySQL 8+

### 2. Configure Environment Variables

Copy `.env.example` (never commit `.env` with real credentials):

| Variable | Description | Default |
|---|---|---|
| `PORT` | Backend server port | `8080` |
| `DB_URL` | MySQL JDBC URL | `jdbc:mysql://localhost:3306/drive_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true` |
| `DB_USERNAME` | MySQL username | `root` |
| `DB_PASSWORD` | MySQL password | `root` |
| `FILE_UPLOAD_DIR` | Local directory for uploaded files | `uploads` |
| `CORS_ALLOWED_ORIGINS` | Allowed frontend origins | `http://localhost:3000,http://localhost:5173` |

### 3. Run the Backend

```powershell
$env:DB_PASSWORD="your_mysql_password"
.\mvnw.cmd spring-boot:run
```

The API server runs at `http://localhost:8080`.

### 4. Run the Frontend

```bash
cd frontend
npm install
npm start
```

The React app runs at `http://localhost:3000` and proxies `/api/*` requests to `http://localhost:8080`.

---

## API Reference

### User Endpoints (`/api/users`)

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/users/register` | Register a new user |
| `POST` | `/api/users/login` | Authenticate user credentials |
| `GET` | `/api/users/{userId}` | Fetch user profile by ID |
| `GET` | `/api/users/username/{username}` | Fetch user profile by username |
| `GET` | `/api/users/all` | List all users |
| `PUT` | `/api/users/{userId}` | Update user profile |
| `DELETE` | `/api/users/{userId}` | Delete user account |

### File & Folder Endpoints (`/api/files`)

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/files/list?parentFolderId=` | List files and folders (root when omitted) |
| `POST` | `/api/files/upload` | Upload a multipart `file` (optional `parentFolderId`) |
| `POST` | `/api/files/folders` | Create a folder (`{ "name": "...", "parentFolderId": null }`) |
| `GET` | `/api/files/downloads/{id}` | Download a file by ID |
| `DELETE` | `/api/files/delete/{fileId}` | Delete a file or empty folder |

---

## Running Tests & Builds

```bash
# Backend unit tests
mvn test

# Frontend production build
cd frontend
npm run build
```

---

## Security Note

Do not commit real database passwords, `.env` files, `uploads/`, or `node_modules/` to version control. Rotate any previously committed database credentials before deploying.
