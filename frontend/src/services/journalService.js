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

export const journalService = {
  getAllEntries: () => {
    return api.get('/journal');
  },
  
  createEntry: (entry) => {
    return api.post('/journal', entry);
  },
  
  updateEntry: (id, entry) => {
    return api.put(`/journal/id/${id}`, entry);
  },
  
  deleteEntry: (id) => {
    return api.delete(`/journal/id/${id}`);
  },
  
  getEntryById: (id) => {
    return api.get(`/journal/id/${id}`);
  }
};
