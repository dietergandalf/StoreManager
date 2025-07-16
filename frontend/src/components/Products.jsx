import React, { useState, useEffect } from 'react';
import { productApi } from '../api';

function Products() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      setLoading(true);
      setError(null);
      const products = await productApi.getAllProducts();
      setProducts(products);
    } catch (err) {
      console.error('Error fetching products:', err);
      setError(`Failed to load products: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(price);
  };

  if (loading) {
    return (
      <div className="card">
        <h2>🛍️ Available Products</h2>
        <p>Loading products...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="card">
        <h2>🛍️ Available Products</h2>
        <div className="error">
          {error}
        </div>
        <button className="btn" onClick={fetchProducts}>
          Try Again
        </button>
      </div>
    );
  }

  return (
    <div className="card">
      <h2>🛍️ Available Products</h2>
      {products.length === 0 ? (
        <p>No products available at the moment.</p>
      ) : (
        <>
          <p>Found {products.length} product(s) in stock</p>
          <div className="products-grid">
            {products.map((productStock) => (
              <div key={productStock.productStockId} className="product-card">
                <div className="product-header">
                  <h3 className="product-name">{productStock.product.name}</h3>
                  <span className="product-price">
                    {formatPrice(productStock.product.price)}
                  </span>
                </div>
                
                <p className="product-description">
                  {productStock.product.description}
                </p>
                
                <div className="product-details">
                  <div className="stock-info">
                    <span className="stock-label">In Stock:</span>
                    <span className={`stock-amount ${productStock.amount < 5 ? 'low-stock' : ''}`}>
                      {productStock.amount} units
                    </span>
                  </div>
                  
                  <div className="seller-info">
                    <span className="seller-label">Sold by:</span>
                    <span className="seller-name">{productStock.sellerName}</span>
                  </div>
                </div>
                
                <div className="product-actions">
                  <button 
                    className="btn btn-primary"
                    disabled={productStock.amount === 0}
                  >
                    {productStock.amount === 0 ? 'Out of Stock' : 'Add to Cart'}
                  </button>
                </div>
              </div>
            ))}
          </div>
        </>
      )}
      
      <div className="products-actions">
        <button className="btn btn-secondary" onClick={fetchProducts}>
          🔄 Refresh Products
        </button>
      </div>
    </div>
  );
}

export default Products;
