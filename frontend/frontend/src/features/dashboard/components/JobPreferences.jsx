import { useEffect, useState } from "react";
import { getJobPreferences } from "../dashboardApi";

const styles = {
  container: {
    maxWidth: "600px",
    margin: "20px auto",
  },

  title: {
    fontSize: "20px",
    fontWeight: "600",
    marginBottom: "20px",
    color: "#111827",
  },

  card: {
    background: "#ffffff",
    borderRadius: "12px",
    padding: "16px",
    marginBottom: "12px",
    boxShadow: "0 6px 18px rgba(0,0,0,0.06)",
    border: "1px solid #e5e7eb",
    transition: "all 0.2s ease",
    cursor: "pointer",
  },

  empty: {
    textAlign: "center",
    padding: "30px",
    color: "#6b7280",
    border: "1px dashed #d1d5db",
    borderRadius: "10px",
  },

  loader: {
    textAlign: "center",
    padding: "20px",
    color: "#6b7280",
  },
};

const JobPreferences = () => {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchJobs();
  }, []);

  const fetchJobs = async () => {
    try {
      setLoading(true);

      const res = await getJobPreferences();

      console.log("API Response:", res.data);

      setJobs(res.data.user?.jobPreferences || []);

    } catch (err) {
      console.error("Error fetching jobs:", err.response?.data || err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.title}>My Job Preferences</h2>

      {/* 🔄 Loading */}
      {loading && <div style={styles.loader}>Loading...</div>}

      {/* ❌ Empty State */}
      {!loading && jobs.length === 0 && (
        <div style={styles.empty}>
          No job preferences found 🚫
        </div>
      )}

      {/* ✅ Job List */}
      {!loading &&
        jobs.map((job, index) => (
          <div
            key={index}
            style={styles.card}
            onMouseEnter={(e) =>
              (e.currentTarget.style.transform = "translateY(-3px)")
            }
            onMouseLeave={(e) =>
              (e.currentTarget.style.transform = "translateY(0)")
            }
          >
            {job}
          </div>
        ))}
    </div>
  );
};

export default JobPreferences;