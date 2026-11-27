import axios from 'axios';

const baseURL = process.env.REACT_APP_API_URL || '';

const apiClient = axios.create({
  baseURL,
});

export function extractErrorMessage(err, fallback) {
  return (
    err?.response?.data?.error ||
    err?.response?.data?.message ||
    (typeof err?.response?.data === 'string' ? err.response.data : null) ||
    fallback
  );
}

export default apiClient;
