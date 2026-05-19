import { useState, useEffect } from "react";
import axios from "axios";

function Orders({ token, username }) {
  const [orders, setOrders] = useState([]);
  const [products, setProducts] = useState([]);
  const [form, setForm] = useState({ productId: "", quantity: "" });
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);

  const fetchOrders = () => {
    axios.get("http://127.0.0.1:8083/api/orders")
      .then(res => setOrders(res.data))
      .catch(() => setError("❌ Could not fetch orders"));
  };

  const fetchProducts = () => {
    axios.get("http://127.0.0.1:8082/api/products")
      .then(res => setProducts(res.data))
      .catch(() => {});
  };

  useEffect(() => {
    fetchOrders();
    fetchProducts();
  }, []);

  const handlePlaceOrder = async () => {
    setMessage(null);
    setError(null);
    if (!username) {
      setError("❌ Please login first before placing an order");
      return;
    }
    try {
      const res = await axios.post("http://127.0.0.1:8083/api/orders", {
        productId: parseInt(form.productId),
        username: username,
        quantity: parseInt(form.quantity),
      });
      if (res.data.status === "CONFIRMED") {
        setMessage("✅ Order placed successfully — CONFIRMED");
      } else {
        setMessage("⚠️ Order placed but FAILED — insufficient stock");
      }
      setForm({ productId: "", quantity: "" });
      fetchOrders();
    } catch {
      setError("❌ Could not place order");
    }
  };

  return (
    <div className="card">
      <h2>Orders</h2>

      {!username && (
        <p className="warning">⚠️ Please login first to place orders</p>
      )}

      <div className="form">
        <h3>Place New Order</h3>
        <select value={form.productId}
          onChange={e => setForm({ ...form, productId: e.target.value })}>
          <option value="">Select a product</option>
          {products.map(p => (
            <option key={p.id} value={p.id}>
              {p.name} — ${p.price} (Stock: {p.stock})
            </option>
          ))}
        </select>
        <input placeholder="Quantity" type="number" value={form.quantity}
          onChange={e => setForm({ ...form, quantity: e.target.value })} />
        <button className="submit-btn" onClick={handlePlaceOrder}>
          Place Order
        </button>
      </div>

      {message && <p className="success">{message}</p>}
      {error && <p className="error">{error}</p>}

      <div className="order-list">
        <h3>All Orders ({orders.length})</h3>
        {orders.length === 0 ? (
          <p className="empty">No orders yet.</p>
        ) : (
          orders.map(o => (
            <div key={o.id} className={`order-item ${o.status === "CONFIRMED" ? "confirmed" : "failed"}`}>
              <div>
                <strong>Order #{o.id}</strong>
                <p>Product ID: {o.productId} | Qty: {o.quantity}</p>
                <p>User: {o.username}</p>
              </div>
              <span className={`order-status ${o.status === "CONFIRMED" ? "confirmed" : "failed"}`}>
                {o.status}
              </span>
            </div>
          ))
        )}
      </div>
    </div>
  );
}

export default Orders;