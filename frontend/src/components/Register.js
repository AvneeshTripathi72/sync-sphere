import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';

function Register() {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
    fullName: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const { username, email, password, confirmPassword, fullName } = formData;

  const onChange = e => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const onSubmit = async e => {
    e.preventDefault();
    setError('');
    setSuccess('');
    
    // Validate passwords match
    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }
    
    setLoading(true);

    try {
      // Create user object without confirmPassword
      const userData = {
        username,
        email,
        password,
        fullName
      };
      
      await axios.post('/api/users/register', userData);
      setSuccess('Registration successful! Redirecting to login...');
      
      // Redirect to login after 2 seconds
      setTimeout(() => {
        navigate('/login');
      }, 2000);
    } catch (err) {
      setError(err.response?.data?.error || 'Registration failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card" style={{ maxWidth: '500px', margin: '0 auto', marginTop: '50px' }}>
      <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>Register</h2>
      
      {error && (
        <div className="alert alert-danger">
          {error}
        </div>
      )}
      
      {success && (
        <div className="alert alert-success">
          {success}
        </div>
      )}
      
      <form onSubmit={onSubmit}>
        <div className="form-group">
          <label htmlFor="username">Username</label>
          <input
            type="text"
            className="form-control"
            id="username"
            name="username"
            value={username}
            onChange={onChange}
            required
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="email">Email</label>
          <input
            type="email"
            className="form-control"
            id="email"
            name="email"
            value={email}
            onChange={onChange}
            required
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="fullName">Full Name</label>
          <input
            type="text"
            className="form-control"
            id="fullName"
            name="fullName"
            value={fullName}
            onChange={onChange}
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="password">Password</label>
          <input
            type="password"
            className="form-control"
            id="password"
            name="password"
            value={password}
            onChange={onChange}
            required
            minLength="6"
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="confirmPassword">Confirm Password</label>
          <input
            type="password"
            className="form-control"
            id="confirmPassword"
            name="confirmPassword"
            value={confirmPassword}
            onChange={onChange}
            required
            minLength="6"
          />
        </div>
        
        <button 
          type="submit" 
          className="btn" 
          style={{ width: '100%', marginTop: '10px' }}
          disabled={loading}
        >
          {loading ? 'Registering...' : 'Register'}
        </button>
      </form>
      
      <p style={{ marginTop: '20px', textAlign: 'center' }}>
        Already have an account? <Link to="/login">Login</Link>
      </p>
    </div>
  );
}

export default Register;