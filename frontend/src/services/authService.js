import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const authService = {
  login: (credentials) => {
    return api.post('/public/login', credentials);
  },
  
  signup: (userData) => {
    return api.post('/public/signup', userData);
  },
  
  verifyToken: (token) => {
    return api.get('/user/profile', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  }
};
