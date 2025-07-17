# Drive Frontend

This is the frontend for the Drive application, a file storage system built with React.

## Features

- User Authentication
  - Registration
  - Login
  - Logout
- File Management
  - Upload files
  - Download files
  - Delete files
  - Navigate folders

## Technologies Used

- React
- React Router for navigation
- Axios for API calls
- CSS for styling

## Project Structure

- `public/`: Static files
- `src/`: Source code
  - `components/`: React components
    - `Navbar.js`: Navigation bar component
    - `Login.js`: Login form component
    - `Register.js`: Registration form component
    - `FileList.js`: Component for displaying and managing files
    - `FileUpload.js`: Component for uploading files
  - `App.js`: Main application component with routing
  - `index.js`: Entry point
  - `index.css`: Global styles

## Setup and Running

1. Make sure you have Node.js and npm installed
2. Install dependencies:
   ```
   npm install
   ```
3. Start the development server:
   ```
   npm start
   ```
4. The application will be available at http://localhost:3000

## API Integration

The frontend communicates with the backend API at http://localhost:8080. The following endpoints are used:

- User Authentication:
  - POST `/api/users/register`: Register a new user
  - POST `/api/users/login`: Login a user
  - GET `/api/users/{userId}`: Get user details
  - PUT `/api/users/{userId}`: Update user details
  - DELETE `/api/users/{userId}`: Delete a user

- File Management:
  - GET `/api/files/list`: Get list of files
  - POST `/api/files/upload`: Upload a file
  - GET `/api/files/downloads/{id}`: Download a file
  - DELETE `/api/files/delete/{fileId}`: Delete a file

## Development Notes

- The application uses React Router for client-side routing
- Authentication state is managed in the App component
- User data is stored in localStorage for persistence
- File upload and download are handled with FormData and Blob APIs