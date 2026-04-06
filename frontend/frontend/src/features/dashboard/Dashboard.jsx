import { useState } from "react";
import JobPreferences from "./components/JobPreferences";
import Notifications from "./components/Notifications";
import AddPreferenceForm from "./components/AddPreferenceForm";

const styles = {
  page: {
    minHeight: "100vh",
    background: "#f8fafc",
    fontFamily: "Inter, system-ui, sans-serif",
  },

  container: {
    maxWidth: "1100px",
    margin: "0 auto",
    padding: "24px 16px",
  },

  header: {
    fontSize: "26px",
    fontWeight: "700",
    marginBottom: "24px",
    color: "#0f172a",
  },

  layout: {
    display: "grid",
    gridTemplateColumns: "2fr 1fr",
    gap: "20px",
  },

  mainCard: {
    background: "#ffffff",
    borderRadius: "16px",
    padding: "22px",
    border: "1px solid #e5e7eb",
    boxShadow: "0 10px 30px rgba(0,0,0,0.05)",
  },

  sideCard: {
    background: "#ffffff",
    borderRadius: "16px",
    padding: "18px",
    border: "1px solid #e5e7eb",
    boxShadow: "0 10px 30px rgba(0,0,0,0.05)",
    height: "fit-content",
    position: "sticky",
    top: "20px",
  },

  tabsContainer: {
    display: "flex",
    gap: "8px",
    marginBottom: "20px",
    position: "relative",
    borderBottom: "1px solid #e5e7eb",
    paddingBottom: "8px",
  },

  tab: {
    padding: "10px 14px",
    borderRadius: "8px",
    cursor: "pointer",
    fontSize: "14px",
    fontWeight: "500",
    color: "#64748b",
    transition: "all 0.25s ease",
  },

  activeTab: {
    color: "#2563eb",
    fontWeight: "600",
    background: "#eff6ff",
  },

  content: {
    marginTop: "10px",
  },

  // Mobile responsive
  mobileLayout: {
    display: "flex",
    flexDirection: "column",
    gap: "20px",
  },
};

const Dashboard = () => {
  const [activeTab, setActiveTab] = useState("form");
  const [hoveredTab, setHoveredTab] = useState(null);

  const isMobile = window.innerWidth < 768;

  return (
    <div style={styles.page}>
      <div style={styles.container}>
        <h1 style={styles.header}>Dashboard</h1>

        <div style={isMobile ? styles.mobileLayout : styles.layout}>

          {/* 🔷 Main Section */}
          <div style={styles.mainCard}>

            {/* 🔥 Tabs */}
            <div style={styles.tabsContainer}>
              {["form", "jobs"].map((tab) => (
                <div
                  key={tab}
                  onClick={() => setActiveTab(tab)}
                  onMouseEnter={() => setHoveredTab(tab)}
                  onMouseLeave={() => setHoveredTab(null)}
                  style={{
                    ...styles.tab,
                    ...(activeTab === tab ? styles.activeTab : {}),
                    ...(hoveredTab === tab && activeTab !== tab
                      ? { background: "#f1f5f9" }
                      : {}),
                  }}
                >
                  {tab === "form" ? "Add Preference" : "My Jobs"}
                </div>
              ))}
            </div>

            {/* 🔄 Dynamic Content */}
            <div style={styles.content}>
              {activeTab === "form" && <AddPreferenceForm />}
              {activeTab === "jobs" && <JobPreferences />}
            </div>
          </div>

          {/* 🔔 Right Side Panel */}
          <div style={styles.sideCard}>
            <Notifications />
          </div>

        </div>
      </div>
    </div>
  );
};

export default Dashboard;