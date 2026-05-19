import { useState } from "react";
import Auth from "./components/Auth";
import Products from "./components/Products";
import Orders from "./components/Orders";
import Status from "./components/Status";
import "./App.css";

function App() {
  const [activeTab, setActiveTab] = useState("status");
  const [token, setToken] = useState(null);
  const [username, setUsername] = useState(null);

  return (
    <div className="app">
      <header>
        <h1>🛒 DevOps E-Commerce Platform</h1>
        <p className="subtitle">Microservices Demo — Spring Boot + Docker + CI/CD</p>
        {username && <p className="user-info">Logged in as: <strong>{username}</strong></p>}
      </header>

      <nav>
        <button onClick={() => setActiveTab("status")} className={activeTab === "status" ? "active" : ""}>
          System Status
        </button>
        <button onClick={() => setActiveTab("auth")} className={activeTab === "auth" ? "active" : ""}>
          {token ? "Logged In ✓" : "Login / Register"}
        </button>
        <button onClick={() => setActiveTab("products")} className={activeTab === "products" ? "active" : ""}>
          Products
        </button>
        <button onClick={() => setActiveTab("orders")} className={activeTab === "orders" ? "active" : ""}>
          Orders
        </button>
      </nav>

      <main>
        {activeTab === "status" && <Status />}
        {activeTab === "auth" && <Auth setToken={setToken} setUsername={setUsername} />}
        {activeTab === "products" && <Products />}
        {activeTab === "orders" && <Orders token={token} username={username} />}
      </main>
    </div>
  );
}

export default App;