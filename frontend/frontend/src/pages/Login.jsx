import { useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate, Link } from "react-router-dom";
import { loginUser } from "../features/auth/authApi";
import { loginSuccess } from "../features/auth/authSlice";

export default function Login() {
  const [form, setForm] = useState({
    username: "",
    password: "",
  });

  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState("");
  const [loading, setLoading] = useState(false);

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const validateField = (name, value) => {
    let error = "";

    if (name === "username") {
      if (!value.trim()) error = "Username is required";
      else if (value.length < 3 || value.length > 50)
        error = "Username must be 3-50 characters";
    }

    if (name === "password") {
      if (!value) error = "Password is required";
      else if (value.length < 6 || value.length > 18)
        error = "Password must be 6-18 characters";
    }

    return error;
  };

  const validateForm = () => {
    let newErrors = {};

    Object.keys(form).forEach((field) => {
      const error = validateField(field, form[field]);
      if (error) newErrors[field] = error;
    });

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;

    setForm((prev) => ({ ...prev, [name]: value }));

    const error = validateField(name, value);

    setErrors((prev) => ({
      ...prev,
      [name]: error,
    }));
  };

  const handleSubmit = async () => {
    setServerError("");

    if (!validateForm()) return;

    try {
      setLoading(true);

      const res = await loginUser(form);
      const token = res?.data?.token;

      if (!token) throw new Error("Token missing");

      dispatch(loginSuccess(token));
      navigate("/dashboard");

    } catch (e) {
      if (e.response) {
        setServerError(e.response.data?.message || "Invalid credentials");
      } else {
        setServerError("Server not reachable");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={styles.title}>Welcome Back 👋</h2>
        <p style={styles.subtitle}>Login to your account</p>

        {/* Username */}
        <div style={styles.inputGroup}>
          <input
            name="username"
            placeholder="Username"
            value={form.username}
            onChange={handleChange}
            style={styles.input}
          />
          {errors.username && <span style={styles.error}>{errors.username}</span>}
        </div>

        {/* Password */}
        <div style={styles.inputGroup}>
          <input
            type="password"
            name="password"
            placeholder="Password"
            value={form.password}
            onChange={handleChange}
            style={styles.input}
          />
          {errors.password && <span style={styles.error}>{errors.password}</span>}
        </div>

        {/* Button */}
        <button
          onClick={handleSubmit}
          disabled={loading}
          style={{
            ...styles.button,
            opacity: loading ? 0.7 : 1,
            cursor: loading ? "not-allowed" : "pointer",
          }}
        >
          {loading ? "Logging in..." : "Login"}
        </button>

        {/* Server Error */}
        {serverError && <p style={styles.serverError}>{serverError}</p>}

        <p style={styles.footer}>
          Don't have an account?{" "}
          <Link to="/register" style={styles.link}>
            Register
          </Link>
        </p>
      </div>
    </div>
  );
}

const styles = {
  container: {
    height: "100vh",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    background: "linear-gradient(135deg, #667eea, #764ba2)",
  },
  card: {
    background: "#fff",
    padding: "40px",
    borderRadius: "12px",
    width: "320px",
    boxShadow: "0 10px 30px rgba(0,0,0,0.2)",
    textAlign: "center",
  },
  title: {
    marginBottom: "5px",
  },
  subtitle: {
    marginBottom: "20px",
    color: "#666",
    fontSize: "14px",
  },
  inputGroup: {
    marginBottom: "15px",
    textAlign: "left",
  },
  input: {
    width: "100%",
    padding: "10px",
    borderRadius: "6px",
    border: "1px solid #ccc",
    outline: "none",
  },
  error: {
    color: "red",
    fontSize: "12px",
  },
  button: {
    width: "100%",
    padding: "10px",
    borderRadius: "6px",
    border: "none",
    background: "#667eea",
    color: "#fff",
    fontWeight: "bold",
    marginTop: "10px",
  },
  serverError: {
    color: "red",
    marginTop: "10px",
  },
  footer: {
    marginTop: "15px",
    fontSize: "14px",
  },
  link: {
    color: "#667eea",
    textDecoration: "none",
    fontWeight: "bold",
  },
};