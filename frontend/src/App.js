import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';

import { useAuth } from './hooks/useAuth';
import Navbar from './components/Navbar';
import Login from './components/Login';
import Register from './components/Register';
import FileList from './components/FileList';
import FileUpload from './components/FileUpload';

function App() {
  const { isAuthenticated, user, loading, login, logout } = useAuth();

  if (loading) {
    return <div className="container">Loading...</div>;
  }

  return (
    <Router>
      <div className="App">
        <Navbar isAuthenticated={isAuthenticated} user={user} logout={logout} />
        <div className="container">
          <Routes>
            <Route
              path="/login"
              element={!isAuthenticated ? <Login login={login} /> : <Navigate to="/" />}
            />
            <Route
              path="/register"
              element={!isAuthenticated ? <Register /> : <Navigate to="/" />}
            />
            <Route
              path="/"
              element={
                isAuthenticated ? (
                  <div>
                    <FileUpload />
                    <FileList />
                  </div>
                ) : (
                  <Navigate to="/login" />
                )
              }
            />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
