import { useEffect, useState } from "react";
import {
  getProfile,
  sendEmailOtp,
  verifyEmailOtp,
  sendPhoneOtp,
  verifyPhoneOtp,
} from "../../api/profileApi";
import Navbar from "../../components/Navbar";

// ================= OTP MODAL =================
const OtpModal = ({ type, onClose, onVerified }) => {
  const [otp, setOtp] = useState("");
  const [step, setStep] = useState("send");
  const [loading, setLoading] = useState(false);
  const [msg, setMsg] = useState({});

  const isEmail = type === "email";

  const handleSend = async () => {
    setLoading(true);
    setMsg({});
    try {
      isEmail ? await sendEmailOtp() : await sendPhoneOtp();
      setStep("verify");
      setMsg({ type: "success", text: "OTP sent successfully" });
    } catch {
      setMsg({ type: "error", text: "Failed to send OTP" });
    } finally {
      setLoading(false);
    }
  };

  const handleVerify = async () => {
    if (!otp) return setMsg({ type: "error", text: "Enter OTP" });

    setLoading(true);
    try {
      isEmail ? await verifyEmailOtp(otp) : await verifyPhoneOtp(otp);
      setMsg({ type: "success", text: "Verified!" });
      setTimeout(() => {
        onVerified();
        onClose();
      }, 700);
    } catch {
      setMsg({ type: "error", text: "Invalid OTP" });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={s.overlay}>
      <div style={s.modal}>
        <h3 style={{ marginBottom: "10px" }}>
          Verify {isEmail ? "Email" : "Phone"}
        </h3>

        {msg.text && (
          <div
            style={
              msg.type === "error" ? s.errorBox : s.successBox
            }
          >
            {msg.text}
          </div>
        )}

        {step === "send" ? (
          <button onClick={handleSend} disabled={loading} style={s.btn}>
            {loading ? "Sending..." : "Send OTP"}
          </button>
        ) : (
          <>
            <input
              value={otp}
              onChange={(e) => setOtp(e.target.value)}
              placeholder="Enter OTP"
              style={s.input}
            />
            <button onClick={handleVerify} style={s.btn}>
              Verify
            </button>
          </>
        )}

        <button onClick={onClose} style={s.closeBtn}>
          Cancel
        </button>
      </div>
    </div>
  );
};

// ================= PROFILE PAGE =================
const ProfilePage = () => {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(null);

  const fetchProfile = async () => {
    try {
      const res = await getProfile();
      setProfile(res.data.user);
    } catch {
      alert("Session expired");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProfile();
  }, []);

  if (loading) return <div style={s.center}>Loading...</div>;

  return (
    <>
      <Navbar />

      <div style={s.page}>
        {modal && (
          <OtpModal
            type={modal}
            onClose={() => setModal(null)}
            onVerified={fetchProfile}
          />
        )}

        <div style={s.card}>
          {/* HEADER */}
          <div style={s.header}>
            <div style={s.avatar}>
              {profile.username?.[0]?.toUpperCase()}
            </div>
            <div>
              <h2 style={{ color: "#f1f5f9" }}>{profile.username}</h2>
              <p style={{ color: "#94a3b8" }}>User Profile</p>
            </div>
          </div>

          {/* DETAILS */}
          <div style={s.section}>
            <ProfileField
              label="Email"
              value={profile.email}
              verified={profile.mailVerified}
              onVerify={() => setModal("email")}
            />

            <ProfileField
              label="Phone"
              value={profile.phoneNumnber}
              verified={profile.phoneVerified}
              onVerify={() => setModal("phone")}
            />
          </div>
        </div>
      </div>
    </>
  );
};

// ================= FIELD =================
const ProfileField = ({ label, value, verified, onVerify }) => (
  <div style={s.field}>
    <div>
      <p style={s.label}>{label}</p>
      <p style={{ color: "#e5e7eb" }}>{value}</p>
    </div>

    {verified ? (
      <span style={s.badgeSuccess}>Verified</span>
    ) : (
      <button style={s.verifyBtn} onClick={onVerify}>
        Verify
      </button>
    )}
  </div>
);

// ================= STYLES =================
const s = {
  page: {
    padding: "40px",
    display: "flex",
    justifyContent: "center",
    background: "#020617",
    minHeight: "100vh",
  },

  card: {
    background: "#0f172a",
    padding: "30px",
    borderRadius: "18px",
    width: "420px",
    border: "1px solid #1e293b",
    boxShadow: "0 20px 50px rgba(0,0,0,0.6)",
  },

  header: {
    display: "flex",
    gap: "15px",
    alignItems: "center",
    marginBottom: "25px",
  },

  avatar: {
    width: "60px",
    height: "60px",
    borderRadius: "50%",
    background: "linear-gradient(135deg, #3b82f6, #8b5cf6)",
    color: "#fff",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    fontSize: "22px",
    fontWeight: "bold",
  },

  section: {
    display: "flex",
    flexDirection: "column",
    gap: "18px",
  },

  field: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    paddingBottom: "12px",
    borderBottom: "1px solid #1e293b",
  },

  label: {
    fontSize: "12px",
    color: "#94a3b8",
  },

  badgeSuccess: {
    background: "rgba(34,197,94,0.15)",
    color: "#4ade80",
    padding: "5px 12px",
    borderRadius: "999px",
    fontSize: "12px",
    fontWeight: "500",
  },

  verifyBtn: {
    background: "#2563eb",
    color: "#fff",
    border: "none",
    padding: "7px 14px",
    borderRadius: "8px",
    cursor: "pointer",
    transition: "0.2s",
  },

  center: {
    textAlign: "center",
    marginTop: "120px",
    color: "#cbd5f5",
  },

  // Modal
  overlay: {
    position: "fixed",
    inset: 0,
    background: "rgba(0,0,0,0.7)",
    backdropFilter: "blur(6px)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    zIndex: 2000,
  },

  modal: {
    background: "#0f172a",
    padding: "25px",
    borderRadius: "14px",
    width: "320px",
    border: "1px solid #1e293b",
    boxShadow: "0 20px 60px rgba(0,0,0,0.7)",
  },

  input: {
    width: "100%",
    padding: "10px",
    marginBottom: "10px",
    background: "#020617",
    border: "1px solid #1e293b",
    borderRadius: "8px",
    color: "#fff",
  },

  btn: {
    width: "100%",
    padding: "10px",
    background: "#2563eb",
    color: "#fff",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
    marginTop: "5px",
  },

  closeBtn: {
    marginTop: "10px",
    width: "100%",
    background: "transparent",
    border: "1px solid #1e293b",
    color: "#94a3b8",
    padding: "8px",
    borderRadius: "8px",
    cursor: "pointer",
  },

  errorBox: {
    background: "rgba(239,68,68,0.15)",
    color: "#f87171",
    padding: "8px",
    borderRadius: "6px",
    marginBottom: "10px",
    fontSize: "13px",
  },

  successBox: {
    background: "rgba(34,197,94,0.15)",
    color: "#4ade80",
    padding: "8px",
    borderRadius: "6px",
    marginBottom: "10px",
    fontSize: "13px",
  },
};

export default ProfilePage;