import { useState, useEffect } from "react";
import axios from "axios";

function Status() {
  const [services, setServices] = useState([
    { name: "User Service", url: "http://127.0.0.1:8081/actuator/health", status: "checking" },
    { name: "Product Service", url: "http://127.0.0.1:8082/actuator/health", status: "checking" },
    { name: "Order Service", url: "http://127.0.0.1:8083/actuator/health", status: "checking" },
  ]);

  useEffect(() => {
    services.forEach((service, index) => {
      axios.get(service.url)
        .then(() => {
          setServices(prev => {
            const updated = [...prev];
            updated[index] = { ...updated[index], status: "UP" };
            return updated;
          });
        })
        .catch(() => {
          setServices(prev => {
            const updated = [...prev];
            updated[index] = { ...updated[index], status: "DOWN" };
            return updated;
          });
        });
    });
  }, []);

  return (
    <div className="card">
      <h2>System Status</h2>
      <div className="status-grid">
        {services.map((service) => (
          <div key={service.name} className="status-item">
            <span className={`status-dot ${service.status === "UP" ? "up" : service.status === "DOWN" ? "down" : "checking"}`}></span>
            <span className="service-name">{service.name}</span>
            <span className={`status-label ${service.status === "UP" ? "up" : service.status === "DOWN" ? "down" : ""}`}>
              {service.status}
            </span>
          </div>
        ))}
      </div>
      <div className="links">
        <a href="http://127.0.0.1:9090" target="_blank" rel="noreferrer">📊 Prometheus</a>
        <a href="http://127.0.0.1:3000" target="_blank" rel="noreferrer">📈 Grafana</a>
      </div>
    </div>
  );
}

export default Status;