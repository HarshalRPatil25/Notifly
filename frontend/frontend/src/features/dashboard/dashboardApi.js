// src/features/dashboard/dashboardApi.js
import { privateApi } from "../../api/axios";

// ✅ Add Job Preferences
export const addJobPreferences = (data) => {
  return privateApi.post("/api/user/job-preference", data);
};

// ✅ Get Job Preferences
export const getJobPreferences = () => {
  return privateApi.get("/api/user/job-preferences");
};

// ✅ Get Notifications
// ✅ Get Notifications
export const getNotifications = () => {
  return privateApi.get("/api/user/notification-received");
};
