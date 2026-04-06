import { privateApi } from "./axios";

// 🔹 Get Profile
export const getProfile = () => {
  return privateApi.get("/api/user/profile");
};

// 🔹 Phone OTP
export const sendPhoneOtp = () => {
  return privateApi.post("/api/user/profile/otp/phone/send");
};

export const verifyPhoneOtp = (otp) => {
  return privateApi.post(
    `/api/user/profile/otp/phone/verify?otp=${otp}`
  );
};

// 🔹 Email OTP
export const sendEmailOtp = () => {
  return privateApi.post("/api/user/profile/otp/email/send");
};

export const verifyEmailOtp = (otp) => {
  return privateApi.post(
    `/api/user/profile/otp/email/verify?otp=${otp}`
  );
};