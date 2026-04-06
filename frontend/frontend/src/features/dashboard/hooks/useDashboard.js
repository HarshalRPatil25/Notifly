// src/features/dashboard/hooks/useDashboard.js

import { useEffect, useState } from "react";
import {
  getJobPreferences,
  getNotifications,
} from "../dashboardApi";

export const useDashboard = () => {
  const [preferences, setPreferences] = useState([]);
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    fetchPreferences();
    fetchNotifications();
  }, []);

  const fetchPreferences = async () => {
    try {
      const res = await getJobPreferences();
      setPreferences(res.data.data || []);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchNotifications = async () => {
    try {
      const res = await getNotifications();
      setNotifications(res.data.data || []);
    } catch (err) {
      console.error(err);
    }
  };

  return { preferences, notifications };
};