import apiClient from './apiClient';

const STORAGE_USER_KEY = 'user';
const STORAGE_TOKEN_KEY = 'token';

export async function login(credentials) {
  const { data } = await apiClient.post('/api/users/login', credentials);
  return data;
}

export async function register(userData) {
  const { data } = await apiClient.post('/api/users/register', userData);
  return data;
}

export function getStoredUser() {
  const token = localStorage.getItem(STORAGE_TOKEN_KEY);
  const rawUser = localStorage.getItem(STORAGE_USER_KEY);
  if (!token || !rawUser) {
    return null;
  }
  try {
    return JSON.parse(rawUser);
  } catch {
    return null;
  }
}

export function persistAuth(userData) {
  localStorage.setItem(STORAGE_USER_KEY, JSON.stringify(userData));
  localStorage.setItem(STORAGE_TOKEN_KEY, 'session-active');
}

export function clearAuth() {
  localStorage.removeItem(STORAGE_USER_KEY);
  localStorage.removeItem(STORAGE_TOKEN_KEY);
}
