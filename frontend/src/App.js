import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import axios from 'axios';

// Components
import Navbar from './components/Navbar';
import Login from './components/Login';
import Register from './components/Register';
import FileList from './components/FileList';
import FileUpload from './components/FileUpload';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Check if user is logged in
    const token = localStorage.getItem('token');
    const userData = localStorage.getItem('user');
    
    if (token && userData) {
      setIsAuthenticated(true);
      setUser(JSON.parse(userData));
    }
    
    setLoading(false);
  }, []);

  const login = (userData) => {
    setIsAuthenticated(true);
    setUser(userData);
    localStorage.setItem('user', JSON.stringify(userData));
    // In a real app, you would store a JWT token here
    localStorage.setItem('token', 'dummy-token');
  };

  const logout = () => {
    setIsAuthenticated(false);
    setUser(null);
    localStorage.removeItem('user');
    localStorage.removeItem('token');
  };

  if (loading) {
    return <div className="container">Loading...</div>;
  }

  return (
    <Router>
      <div className="App">
        <Navbar isAuthenticated={isAuthenticated} user={user} logout={logout} />
        <div className="container">
          <Routes>
            <Route path="/login" element={
              !isAuthenticated ? <Login login={login} /> : <Navigate to="/" />
            } />
            <Route path="/register" element={
              !isAuthenticated ? <Register /> : <Navigate to="/" />
            } />
            <Route path="/" element={
              isAuthenticated ? (
                <div>
                  <FileUpload />
                  <FileList />
                </div>
              ) : <Navigate to="/login" />
            } />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;