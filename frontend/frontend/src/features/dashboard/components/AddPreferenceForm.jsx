import { useState } from "react";
import { addJobPreferences } from "../dashboardApi";

const styles = {
  container: {
    maxWidth: "480px",
    margin: "40px auto",
    background: "#ffffff",
    padding: "28px",
    borderRadius: "16px",
    boxShadow: "0 10px 30px rgba(0,0,0,0.08)",
    border: "1px solid #e5e7eb",
    fontFamily: "Inter, system-ui, sans-serif",
  },

  title: {
    fontSize: "20px",
    fontWeight: "600",
    marginBottom: "24px",
    color: "#111827",
    textAlign: "center",
  },

  formGroup: {
    marginBottom: "18px",
    display: "flex",
    flexDirection: "column",
  },

  label: {
    fontSize: "13px",
    fontWeight: "500",
    marginBottom: "6px",
    color: "#374151",
  },

  input: {
    padding: "11px 12px",
    borderRadius: "10px",
    border: "1px solid #d1d5db",
    fontSize: "14px",
    outline: "none",
    transition: "all 0.25s ease",
    backgroundColor: "#fafafa",
  },

  inputFocus: {
    border: "1px solid #6366f1",
    boxShadow: "0 0 0 3px rgba(99,102,241,0.15)",
    backgroundColor: "#fff",
  },

  button: {
    width: "100%",
    padding: "12px",
    background: "linear-gradient(135deg, #3b82f6, #6366f1)",
    color: "#fff",
    border: "none",
    borderRadius: "10px",
    fontSize: "14px",
    fontWeight: "600",
    cursor: "pointer",
    transition: "all 0.25s ease",
    marginTop: "10px",
  },

  buttonHover: {
    transform: "translateY(-1px)",
    boxShadow: "0 6px 18px rgba(99,102,241,0.25)",
  },

  buttonActive: {
    transform: "scale(0.98)",
  },
};

const AddPreferenceForm = () => {
  const [formData, setFormData] = useState({
    jobTitle: "",
    location: "",
    salary: "",
    primaryLanguage: "",
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleFocus = (e) => {
    e.target.style.border = "1px solid #3b82f6";
    e.target.style.boxShadow = "0 0 0 2px rgba(59,130,246,0.2)";
  };

  const handleBlur = (e) => {
    e.target.style.border = "1px solid #d1d5db";
    e.target.style.boxShadow = "none";
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const data = [formData];

    try {
      await addJobPreferences(data);
      alert("Added successfully ✅");

      setFormData({
        jobTitle: "",
        location: "",
        salary: "",
        primaryLanguage: "",
      });

    } catch (err) {
      console.error("Error:", err.response?.data || err.message);
    }
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.title}>Add Job Preference</h2>

      <form onSubmit={handleSubmit}>

        {/* Job Title */}
        <div style={styles.formGroup}>
          <label style={styles.label}>Job Title *</label>
          <input
            type="text"
            name="jobTitle"
            placeholder="e.g. Frontend Developer"
            value={formData.jobTitle}
            onChange={handleChange}
            onFocus={handleFocus}
            onBlur={handleBlur}
            style={styles.input}
            required
          />
        </div>

        {/* Location */}
        <div style={styles.formGroup}>
          <label style={styles.label}>Location</label>
          <input
            type="text"
            name="location"
            placeholder="e.g. Remote / Mumbai"
            value={formData.location}
            onChange={handleChange}
            onFocus={handleFocus}
            onBlur={handleBlur}
            style={styles.input}
          />
        </div>

        {/* Salary */}
        <div style={styles.formGroup}>
          <label style={styles.label}>Salary Expectation</label>
          <input
            type="text"
            name="salary"
            placeholder="e.g. 10 LPA"
            value={formData.salary}
            onChange={handleChange}
            onFocus={handleFocus}
            onBlur={handleBlur}
            style={styles.input}
          />
        </div>

        {/* Primary Language */}
        <div style={styles.formGroup}>
          <label style={styles.label}>Primary Coding Skill</label>
          <input
            type="text"
            name="primaryLanguage"
            placeholder="e.g. React / Java"
            value={formData.primaryLanguage}
            onChange={handleChange}
            onFocus={handleFocus}
            onBlur={handleBlur}
            style={styles.input}
          />
        </div>

        {/* Submit */}
        <button
          type="submit"
          style={styles.button}
          onMouseEnter={(e) =>
            (e.target.style.opacity = "0.9")
          }
          onMouseLeave={(e) =>
            (e.target.style.opacity = "1")
          }
        >
          Add Preference
        </button>

      </form>
    </div>
  );
};

export default AddPreferenceForm;