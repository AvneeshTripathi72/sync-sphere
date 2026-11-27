import React from 'react';
import { Link } from 'react-router-dom';

function Navbar({ isAuthenticated, user, logout }) {
  return (
    <nav
      className="navbar"
      style={{
        backgroundColor: '#4285f4',
        color: 'white',
        padding: '10px 20px',
        marginBottom: '20px',
      }}
    >
      <div
        className="container"
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
        }}
      >
        <Link to="/" style={{ color: 'white', textDecoration: 'none', fontSize: '1.5rem' }}>
          SyncSphere Drive
        </Link>
        <div>
          {isAuthenticated ? (
            <div style={{ display: 'flex', alignItems: 'center' }}>
              <span style={{ marginRight: '15px' }}>Welcome, {user?.username || 'User'}</span>
              <button
                onClick={logout}
                className="btn"
                style={{
                  backgroundColor: 'white',
                  color: '#4285f4',
                  border: 'none',
                }}
              >
                Logout
              </button>
            </div>
          ) : (
            <div>
              <Link
                to="/login"
                style={{ color: 'white', textDecoration: 'none', marginRight: '15px' }}
              >
                Login
              </Link>
              <Link to="/register" style={{ color: 'white', textDecoration: 'none' }}>
                Register
              </Link>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
