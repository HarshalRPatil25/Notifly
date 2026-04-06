// src/api/axios.js
import axios from "axios";

const BASE_URL = "http://localhost:8080";

// ✅ Public API (NO TOKEN)
export const publicApi = axios.create({
  baseURL: BASE_URL,
});

// ✅ Private API (WITH TOKEN)
export const privateApi = axios.create({
  baseURL: BASE_URL,
});

// 🔐 Attach token ONLY for private API
privateApi.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);