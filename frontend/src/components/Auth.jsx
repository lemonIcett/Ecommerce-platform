import { useState } from "react";
import axios from "axios";

function Auth({ setToken, setUsername }) {
  const [mode, setMode] = useState("login");
  const [form, setForm] = useState({ username: "", email: "", password: "" });
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);

  const handleSubmit = async () => {
    setMessage(null);
    setError(null);
    try {
      const url = mode === "login"
        ? "http://127.0.0.1:8081/api/auth/login"
        : "http://127.0.0.1:8081/api/auth/register";

      const payload = mode === "login"
        ? { username: form.username, password: form.password }
        : { username: form.username, email: form.email, password: form.password };

      const res = await axios.post(url, payload);
      setToken(res.data.token);
      setUsername(res.data.username);
      setMessage(`✅ ${mode === "login" ? "Login" : "Registration"} successful! Welcome ${res.data.username}`);
    } catch (err) {
      setError(`❌ ${err.response?.data?.message || "Something went wrong"}`);
    }
  };

  return (
    <div className="card">
      <h2>{mode === "login" ? "Login" : "Register"}</h2>
      <div className="tab-switch">
        <button onClick={() => setMode("login")} className={mode === "login" ? "active" : ""}>Login</button>
        <button onClick={() => setMode("register")} className={mode === "register" ? "active" : ""}>Register</button>
      </div>

      <div className="form">
        <input
          placeholder="Username"
          value={form.username}
          onChange={e => setForm({ ...form, username: e.target.value })}
        />
        {mode === "register" && (
          <input
            placeholder="Email"
            value={form.email}
            onChange={e => setForm({ ...form, email: e.target.value })}
          />
        )}
        <input
          placeholder="Password"
          type="password"
          value={form.password}
          onChange={e => setForm({ ...form, password: e.target.value })}
        />
        <button className="submit-btn" onClick={handleSubmit}>
          {mode === "login" ? "Login" : "Register"}
        </button>
      </div>

      {message && <p className="success">{message}</p>}
      {error && <p className="error">{error}</p>}
    </div>
  );
}

export default Auth;