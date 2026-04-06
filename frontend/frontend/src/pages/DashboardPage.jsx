import Navbar from "../components/Navbar";

import Dashboard from "../features/dashboard/Dashboard";

const DashboardPage = () => {
  return (
    <div style={{ display: "flex" }}>

      {/* ✅ Sidebar was imported but never rendered */}
     

      <div style={{ flex: 1, background: "#f3f4f6", minHeight: "100vh" }}>
        <Navbar />
        <Dashboard />
      </div>

    </div>
  );
};

export default DashboardPage;