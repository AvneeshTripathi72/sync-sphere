import { useState, useEffect, useCallback } from 'react';
import { getStoredUser, persistAuth, clearAuth } from '../services/authService';

export function useAuth() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const storedUser = getStoredUser();
    if (storedUser) {
      setUser(storedUser);
    }
    setLoading(false);
  }, []);

  const login = useCallback((userData) => {
    setUser(userData);
    persistAuth(userData);
  }, []);

  const logout = useCallback(() => {
    setUser(null);
    clearAuth();
  }, []);

  return {
    user,
    isAuthenticated: Boolean(user),
    loading,
    login,
    logout,
  };
}
