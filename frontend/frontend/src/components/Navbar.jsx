import { useState } from "react";
import { Bell, LogOut, User } from "lucide-react";
import { useNavigate } from "react-router-dom";

const styles = {
  container: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    background: "rgba(17, 24, 39, 0.9)", // dark bg
    backdropFilter: "blur(12px)",
    padding: "12px 24px",
    borderBottom: "1px solid #1f2937",
    boxShadow: "0 2px 8px rgba(0,0,0,0.4)",
    position: "sticky",
    top: 0,
    zIndex: 1000,
  },
  left: {
    display: "flex",
    alignItems: "center",
    gap: "12px",
  },
  logo: {
    background: "linear-gradient(to right, #2563eb, #7c3aed)",
    color: "#fff",
    padding: "6px 12px",
    borderRadius: "10px",
    fontWeight: "700",
    fontSize: "14px",
  },
  title: {
    fontSize: "16px",
    fontWeight: "600",
    color: "#e5e7eb",
  },
  right: {
    display: "flex",
    alignItems: "center",
    gap: "22px",
  },
  bellWrapper: {
    position: "relative",
    cursor: "pointer",
    padding: "6px",
    borderRadius: "8px",
    transition: "background 0.2s",
  },
  bellHover: {
    background: "#1f2937",
  },
  badge: {
    position: "absolute",
    top: "-5px",
    right: "-6px",
    background: "#ef4444",
    color: "#fff",
    fontSize: "10px",
    padding: "2px 6px",
    borderRadius: "999px",
    fontWeight: "600",
  },
  userWrapper: {
    position: "relative",
  },
  userButton: {
    display: "flex",
    alignItems: "center",
    gap: "8px",
    cursor: "pointer",
    padding: "6px 10px",
    borderRadius: "10px",
    transition: "background 0.2s",
  },
  userHover: {
    background: "#1f2937",
  },
  avatar: {
    background: "#374151",
    padding: "6px",
    borderRadius: "50%",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
  },
  username: {
    fontSize: "14px",
    fontWeight: "500",
    color: "#d1d5db",
  },
  dropdown: {
    position: "absolute",
    right: 0,
    marginTop: "12px",
    width: "170px",
    background: "#111827",
    border: "1px solid #1f2937",
    borderRadius: "12px",
    boxShadow: "0 10px 25px rgba(0,0,0,0.5)",
    overflow: "hidden",
  },
  dropdownItem: {
    display: "flex",
    alignItems: "center",
    gap: "10px",
    padding: "12px 14px",
    cursor: "pointer",
    fontSize: "14px",
    color: "#e5e7eb",
    transition: "background 0.2s",
  },
  dropdownItemHover: {
    background: "#1f2937",
  },
  logoutItem: {
    color: "#f87171",
  },
};

const Navbar = () => {
  const [open, setOpen] = useState(false);
  const [hoverBell, setHoverBell] = useState(false);
  const [hoverUser, setHoverUser] = useState(false);
  const [hoverItem, setHoverItem] = useState(null);

  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <div style={styles.container}>
      {/* Left */}
      <div style={styles.left}>
        <div style={styles.logo}>N</div>
        <h1 style={styles.title}>Notifly Dashboard</h1>
      </div>

      {/* Right */}
      <div style={styles.right}>
        {/* Notification */}
        <div
          style={{
            ...styles.bellWrapper,
            ...(hoverBell ? styles.bellHover : {}),
          }}
          onMouseEnter={() => setHoverBell(true)}
          onMouseLeave={() => setHoverBell(false)}
        >
          <Bell size={20} color="#9ca3af" />
          <span style={styles.badge}>1</span>
        </div>

        {/* User */}
        <div style={styles.userWrapper}>
          <div
            style={{
              ...styles.userButton,
              ...(hoverUser ? styles.userHover : {}),
            }}
            onClick={() => setOpen(!open)}
            onMouseEnter={() => setHoverUser(true)}
            onMouseLeave={() => setHoverUser(false)}
          >
            <div style={styles.avatar}>
              <User size={16} color="#d1d5db" />
            </div>
            <span style={styles.username}>User</span>
          </div>

          {open && (
            <div style={styles.dropdown}>
              {/* Profile */}
              <div
                style={{
                  ...styles.dropdownItem,
                  ...(hoverItem === "profile"
                    ? styles.dropdownItemHover
                    : {}),
                }}
                onMouseEnter={() => setHoverItem("profile")}
                onMouseLeave={() => setHoverItem(null)}
                onClick={() => {
                  navigate("/profile");
                  setOpen(false);
                }}
              >
                <User size={16} />
                Profile
              </div>

              {/* Logout */}
              <div
                style={{
                  ...styles.dropdownItem,
                  ...styles.logoutItem,
                  ...(hoverItem === "logout"
                    ? styles.dropdownItemHover
                    : {}),
                }}
                onMouseEnter={() => setHoverItem("logout")}
                onMouseLeave={() => setHoverItem(null)}
                onClick={handleLogout}
              >
                <LogOut size={16} />
                Logout
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Navbar;