# Drive - File Storage Application

A full-stack file storage application with user authentication and file management capabilities.

## Project Overview

This project consists of two main parts:
1. **Backend**: A Spring Boot application that provides RESTful APIs for user authentication and file management.
2. **Frontend**: A React application that provides a user interface for interacting with the backend.

## Features

- User Authentication
  - Registration
  - Login/Logout
  - User profile management
- File Management
  - Upload files
  - Download files
  - Delete files
  - Navigate folders

## Technology Stack

### Backend
- Java 21
- Spring Boot 3.5.3
- Spring Data JPA
- MySQL Database
- Maven for dependency management

### Frontend
- React
- React Router
- Axios
- CSS

## Project Structure

- `src/main/java/com/example/drive/`: Backend source code
  - `controler/`: REST controllers
  - `entity/`: JPA entities
  - `repo/`: JPA repositories
  - `services/`: Business logic
- `frontend/`: React frontend application
  - `src/`: Source code
    - `components/`: React components
    - `App.js`: Main application component
    - `index.js`: Entry point

## Setup and Running

### Prerequisites
- Java 21
- Node.js and npm
- MySQL

### Backend Setup
1. Configure your MySQL database in `src/main/resources/application.properties`
2. Build the project:
   ```
   mvn clean install
   ```
3. Run the Spring Boot application:
   ```
   mvn spring-boot:run
   ```
   The backend will be available at http://localhost:8080

### Frontend Setup
1. Navigate to the frontend directory:
   ```
   cd frontend
   ```
2. Install dependencies:
   ```
   npm install
   ```
3. Start the development server:
   ```
   npm start
   ```
   The frontend will be available at http://localhost:3000

## API Documentation

### User Authentication Endpoints
- POST `/api/users/register`: Register a new user
- POST `/api/users/login`: Login a user
- GET `/api/users/{userId}`: Get user details
- PUT `/api/users/{userId}`: Update user details
- DELETE `/api/users/{userId}`: Delete a user

### File Management Endpoints
- GET `/api/files/list`: Get list of files
- POST `/api/files/upload`: Upload a file
- GET `/api/files/downloads/{id}`: Download a file
- DELETE `/api/files/delete/{fileId}`: Delete a file

## Development Notes

- The backend uses Spring Data JPA for database operations
- File storage is handled on the server's file system
- Authentication is currently handled with simple username/password validation
- The frontend uses React Router for client-side routing
- User data is stored in localStorage for persistence