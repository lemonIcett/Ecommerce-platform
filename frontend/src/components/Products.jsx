import { useState, useEffect } from "react";
import axios from "axios";

function Products() {
  const [products, setProducts] = useState([]);
  const [form, setForm] = useState({ name: "", description: "", price: "", stock: "" });
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);

  const fetchProducts = () => {
    axios.get("http://127.0.0.1:8082/api/products")
      .then(res => setProducts(res.data))
      .catch(() => setError("❌ Could not fetch products"));
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  const handleCreate = async () => {
    setMessage(null);
    setError(null);
    try {
      await axios.post("http://127.0.0.1:8082/api/products", {
        name: form.name,
        description: form.description,
        price: parseFloat(form.price),
        stock: parseInt(form.stock),
      });
      setMessage("✅ Product created successfully");
      setForm({ name: "", description: "", price: "", stock: "" });
      fetchProducts();
    } catch (err) {
      setError("❌ Could not create product");
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`http://127.0.0.1:8082/api/products/${id}`);
      setMessage("✅ Product deleted");
      fetchProducts();
    } catch {
      setError("❌ Could not delete product");
    }
  };

  return (
    <div className="card">
      <h2>Products</h2>

      <div className="form">
        <h3>Add New Product</h3>
        <input placeholder="Name" value={form.name}
          onChange={e => setForm({ ...form, name: e.target.value })} />
        <input placeholder="Description" value={form.description}
          onChange={e => setForm({ ...form, description: e.target.value })} />
        <input placeholder="Price" type="number" value={form.price}
          onChange={e => setForm({ ...form, price: e.target.value })} />
        <input placeholder="Stock" type="number" value={form.stock}
          onChange={e => setForm({ ...form, stock: e.target.value })} />
        <button className="submit-btn" onClick={handleCreate}>Add Product</button>
      </div>

      {message && <p className="success">{message}</p>}
      {error && <p className="error">{error}</p>}

      <div className="product-list">
        <h3>All Products ({products.length})</h3>
        {products.length === 0 ? (
          <p className="empty">No products yet. Add one above.</p>
        ) : (
          products.map(p => (
            <div key={p.id} className="product-item">
              <div>
                <strong>{p.name}</strong>
                <p>{p.description}</p>
                <span className="price">${p.price}</span>
                <span className="stock"> | Stock: {p.stock}</span>
              </div>
              <button className="delete-btn" onClick={() => handleDelete(p.id)}>Delete</button>
            </div>
          ))
        )}
      </div>
    </div>
  );
}

export default Products;