// src/features/auth/authApi.js
import { publicApi } from "../../api/axios";

// ✅ REGISTER (NO TOKEN)
export const registerUser = (data) => {
  return publicApi.post("/api/public/register", data);
};

// ✅ LOGIN (NO TOKEN)
export const loginUser = (data) => {
  return publicApi.post("/api/public/login", data);
};