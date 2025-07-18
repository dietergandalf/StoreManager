import React, { useState, useEffect, useCallback } from 'react';
import { customerApi, productApi } from '../api/storeApi';
import Checkout from './Checkout';
import '../styles/ShoppingCart.css';

const ShoppingCart = ({ customerId }) => {
  const [cart, setCart] = useState(null);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showAddProducts, setShowAddProducts] = useState(false);
  const [showCheckout, setShowCheckout] = useState(false);

  const loadCart = useCallback(async () => {
    try {
      setLoading(true);
      const cartData = await customerApi.getCart(customerId);
      setCart(cartData);
      setError(null);
    } catch (err) {
      // If cart doesn't exist or is empty, that's okay
      if (err.response?.status === 404) {
        setCart(null);
      } else {
        setError('Failed to load cart');
        console.error('Cart loading error:', err);
      }
    } finally {
      setLoading(false);
    }
  }, [customerId]);

  const loadProducts = useCallback(async () => {
    try {
      const productsData = await productApi.getAllProducts();
      setProducts(productsData);
    } catch (err) {
      console.error('Failed to load products:', err);
    }
  }, []);

  useEffect(() => {
    loadCart();
    loadProducts();
  }, [loadCart, loadProducts]);

  const addToCart = async (productStockId, quantity = 1) => {
    try {
      const cartData = await customerApi.addToCart(customerId, {
        productStockId,
        quantity
      });
      setCart(cartData);
      setError(null);
      alert('Product added to cart!');
    } catch (err) {
      setError('Failed to add product to cart: ' + (err.response?.data?.message || err.message));
      console.error('Add to cart error:', err);
    }
  };

  const removeFromCart = async (cartItemId) => {
    try {
      const cartData = await customerApi.removeFromCart(customerId, cartItemId);
      setCart(cartData);
      setError(null);
    } catch (err) {
      setError('Failed to remove item from cart');
      console.error('Remove from cart error:', err);
    }
  };

  const updateQuantity = async (cartItemId, newQuantity) => {
    if (newQuantity <= 0) {
      await removeFromCart(cartItemId);
      return;
    }

    try {
      const cartData = await customerApi.updateCartItemQuantity(customerId, cartItemId, newQuantity);
      setCart(cartData);
      setError(null);
    } catch (err) {
      setError('Failed to update quantity');
      console.error('Update quantity error:', err);
    }
  };

  const clearCart = async () => {
    if (!window.confirm('Are you sure you want to clear your cart?')) {
      return;
    }

    try {
      await customerApi.clearCart(customerId);
      setCart(null);
      setError(null);
    } catch (err) {
      setError('Failed to clear cart');
      console.error('Clear cart error:', err);
    }
  };

  const handleProceedToCheckout = () => {
    if (cart && cart.cartItems && cart.cartItems.length > 0) {
      setShowCheckout(true);
    }
  };

  const handleOrderSuccess = (order, targetTab) => {
    console.log(`Order #${order.orderId} placed successfully!`);
    setShowCheckout(false);
    setCart(null); // Cart is cleared after successful order
    loadCart(); // Reload cart to sync with backend
    
    // If targetTab is specified, we could pass this up to switch tabs
    if (targetTab === 'orders') {
      // For now, just show a message about viewing orders
      alert(`Order #${order.orderId} placed successfully! You can view your orders in the Order History tab.`);
    } else {
      alert(`Order #${order.orderId} placed successfully! Thank you for your purchase.`);
    }
  };

  const handleCancelCheckout = () => {
    setShowCheckout(false);
  };

  if (loading) {
    return (
      <div className="tab-content">
        <h3>Shopping Cart</h3>
        <div className="loading">Loading cart...</div>
      </div>
    );
  }

  // Show checkout page if user clicked proceed to checkout
  if (showCheckout) {
    return (
      <Checkout 
        customerId={customerId}
        onOrderSuccess={handleOrderSuccess}
        onCancel={handleCancelCheckout}
      />
    );
  }

  return (
    <div className="tab-content">
      <div className="shopping-cart-header">
        <h3>Shopping Cart</h3>
        <div className="cart-actions">
          <button 
            className="btn btn-primary"
            onClick={() => setShowAddProducts(!showAddProducts)}
          >
            {showAddProducts ? 'Hide Products' : 'Add Products'}
          </button>
          {cart && cart.cartItems && cart.cartItems.length > 0 && (
            <button 
              className="btn btn-danger"
              onClick={clearCart}
            >
              Clear Cart
            </button>
          )}
        </div>
      </div>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      {/* Add Products Section */}
      {showAddProducts && (
        <div className="add-products-section">
          <h4>Available Products</h4>
          <div className="products-grid">
            {products.map((product) => (
              <div key={product.productStockId} className="product-card">
                <h5>{product.product?.name || 'Product'}</h5>
                <p className="product-description">{product.product?.description}</p>
                <div className="product-info">
                  <span className="price">${product.product?.price?.toFixed(2)}</span>
                  <span className="stock">Stock: {product.amount}</span>
                </div>
                <button 
                  className="btn btn-primary btn-sm"
                  onClick={() => addToCart(product.productStockId, 1)}
                  disabled={product.amount <= 0}
                >
                  {product.amount > 0 ? 'Add to Cart' : 'Out of Stock'}
                </button>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Cart Contents */}
      {cart && cart.cartItems && cart.cartItems.length > 0 ? (
        <div className="cart-contents">
          <div className="cart-summary">
            <p><strong>Total Items:</strong> {cart.totalItems}</p>
            <p><strong>Total Amount:</strong> ${cart.totalAmount?.toFixed(2)}</p>
          </div>

          <div className="cart-items">
            {cart.cartItems.map((item) => (
              <div key={item.cartItemId} className="cart-item">
                <div className="item-info">
                  <h5>{item.productStock?.product?.name || 'Product'}</h5>
                  <p>{item.productStock?.product?.description}</p>
                  <p className="item-price">Unit Price: ${item.priceAtTimeOfAdd?.toFixed(2)}</p>
                </div>
                
                <div className="item-controls">
                  <div className="quantity-controls">
                    <button 
                      className="btn btn-sm"
                      onClick={() => updateQuantity(item.cartItemId, item.quantity - 1)}
                    >
                      -
                    </button>
                    <span className="quantity">{item.quantity}</span>
                    <button 
                      className="btn btn-sm"
                      onClick={() => updateQuantity(item.cartItemId, item.quantity + 1)}
                    >
                      +
                    </button>
                  </div>
                  
                  <div className="item-total">
                    <strong>${item.totalPrice?.toFixed(2)}</strong>
                  </div>
                  
                  <button 
                    className="btn btn-danger btn-sm"
                    onClick={() => removeFromCart(item.cartItemId)}
                  >
                    Remove
                  </button>
                </div>
              </div>
            ))}
          </div>

          <div className="cart-total">
            <h4>Total: ${cart.totalAmount?.toFixed(2)}</h4>
            <button 
              className="btn btn-success btn-lg"
              onClick={handleProceedToCheckout}
            >
              Proceed to Checkout
            </button>
          </div>
        </div>
      ) : (
        <div className="empty-cart">
          <p>Your cart is empty.</p>
          {!showAddProducts && (
            <button 
              className="btn btn-primary"
              onClick={() => setShowAddProducts(true)}
            >
              Browse Products
            </button>
          )}
        </div>
      )}
    </div>
  );
};

export default ShoppingCart;
