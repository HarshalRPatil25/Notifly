import { useEffect, useState } from "react";
import { getNotifications } from "../dashboardApi";
import { Mail, MessageCircle } from "lucide-react";

const styles = {
  container: {
    maxWidth: "640px",
    margin: "40px auto",
    padding: "0 16px",
    fontFamily: "Inter, system-ui, sans-serif",
  },

  title: {
    fontSize: "22px",
    fontWeight: "600",
    marginBottom: "24px",
    color: "#111827",
  },

  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fit, minmax(220px, 1fr))",
    gap: "16px",
  },

  card: {
    background: "#ffffff",
    borderRadius: "14px",
    padding: "22px",
    border: "1px solid #e5e7eb",
    boxShadow: "0 8px 24px rgba(0,0,0,0.06)",
    textAlign: "center",
    transition: "all 0.25s ease",
  },

  cardHover: {
    transform: "translateY(-5px)",
    boxShadow: "0 12px 30px rgba(0,0,0,0.12)",
  },

  iconWrapper: {
    marginBottom: "12px",
  },

  label: {
    fontSize: "14px",
    color: "#6b7280",
    marginBottom: "6px",
  },

  count: {
    fontSize: "28px",
    fontWeight: "700",
    color: "#111827",
  },

  skeleton: {
    height: "120px",
    borderRadius: "14px",
    background: "linear-gradient(90deg, #f3f4f6 25%, #e5e7eb 37%, #f3f4f6 63%)",
    backgroundSize: "400% 100%",
    animation: "shimmer 1.4s ease infinite",
  },
};

// Inject shimmer animation once
if (typeof document !== "undefined") {
  const styleSheet = document.styleSheets[0];
  if (styleSheet) {
    const keyframes = `
      @keyframes shimmer {
        0% { background-position: 100% 0 }
        100% { background-position: -100% 0 }
      }
    `;
    styleSheet.insertRule(keyframes, styleSheet.cssRules.length);
  }
}

const Notifications = () => {
  const [emailCount, setEmailCount] = useState(0);
  const [whatsappCount, setWhatsappCount] = useState(0);
  const [loading, setLoading] = useState(true);
  const [hovered, setHovered] = useState(null);

  useEffect(() => {
    fetchNotifications();
  }, []);

  const fetchNotifications = async () => {
    try {
      setLoading(true);
      const res = await getNotifications();

      const data = res.data.notificationReceived || [];

      setEmailCount(Number(data[0]) || 0);
      setWhatsappCount(Number(data[1]) || 0);

    } catch (err) {
      console.error("Error:", err.response?.data || err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.title}>Notifications</h2>

      {loading ? (
        <div style={styles.grid}>
          {[1, 2].map((i) => (
            <div key={i} style={styles.skeleton}></div>
          ))}
        </div>
      ) : (
        <div style={styles.grid}>

          {/* 📧 Email */}
          <div
            style={{
              ...styles.card,
              ...(hovered === "email" && styles.cardHover),
            }}
            onMouseEnter={() => setHovered("email")}
            onMouseLeave={() => setHovered(null)}
          >
            <div style={styles.iconWrapper}>
              <Mail size={30} color="#3b82f6" />
            </div>

            <div style={styles.label}>Email Notifications</div>
            <div style={styles.count}>{emailCount}</div>
          </div>

          {/* 📱 WhatsApp */}
          <div
            style={{
              ...styles.card,
              ...(hovered === "whatsapp" && styles.cardHover),
            }}
            onMouseEnter={() => setHovered("whatsapp")}
            onMouseLeave={() => setHovered(null)}
          >
            <div style={styles.iconWrapper}>
              <MessageCircle size={30} color="#22c55e" />
            </div>

            <div style={styles.label}>WhatsApp Notifications</div>
            <div style={styles.count}>{whatsappCount}</div>
          </div>

        </div>
      )}
    </div>
  );
};

export default Notifications;