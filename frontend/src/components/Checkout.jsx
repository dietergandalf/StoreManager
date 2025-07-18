import React, { useState, useEffect } from 'react';
import { customerApi } from '../api/storeApi';
import OrderConfirmation from './OrderConfirmation';
import '../styles/Checkout.css';

const Checkout = ({ customerId, onOrderSuccess, onCancel }) => {
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
  const [processing, setProcessing] = useState(false);
  const [error, setError] = useState('');
  const [completedOrder, setCompletedOrder] = useState(null);
  const [formData, setFormData] = useState({
    shippingAddress: '',
    billingAddress: '',
    paymentMethod: 'credit_card',
    orderNotes: ''
  });

  useEffect(() => {
    loadCart();
  }, [customerId]);

  const loadCart = async () => {
    try {
      setLoading(true);
      const cartData = await customerApi.getCart(customerId);
      setCart(cartData);
    } catch (error) {
      console.error('Error loading cart:', error);
      setError('Failed to load cart. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSameAsBilling = (e) => {
    if (e.target.checked) {
      setFormData(prev => ({
        ...prev,
        billingAddress: prev.shippingAddress
      }));
    }
  };

  const validateForm = () => {
    if (!formData.shippingAddress.trim()) {
      setError('Shipping address is required');
      return false;
    }
    if (!formData.billingAddress.trim()) {
      setError('Billing address is required');
      return false;
    }
    return true;
  };

  const handleCheckout = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }

    setProcessing(true);
    setError('');

    try {
      const order = await customerApi.checkout(customerId, formData);
      console.log('Order created successfully:', order);
      
      // Store the completed order to show confirmation
      setCompletedOrder(order);
      
    } catch (error) {
      console.error('Error during checkout:', error);
      setError('Checkout failed. Please check your information and try again.');
    } finally {
      setProcessing(false);
    }
  };

  const handleContinueShopping = () => {
    if (onOrderSuccess) {
      onOrderSuccess(completedOrder);
    }
  };

  const handleViewOrders = () => {
    if (onOrderSuccess) {
      onOrderSuccess(completedOrder, 'orders'); // Pass a flag to switch to orders tab
    }
  };

  // Show order confirmation if order was completed
  if (completedOrder) {
    return (
      <OrderConfirmation 
        order={completedOrder}
        onContinueShopping={handleContinueShopping}
        onViewOrders={handleViewOrders}
      />
    );
  }

  if (loading) {
    return (
      <div className="checkout-container">
        <div className="loading">Loading checkout...</div>
      </div>
    );
  }

  if (!cart || !cart.cartItems || cart.cartItems.length === 0) {
    return (
      <div className="checkout-container">
        <div className="empty-cart">
          <h2>Your cart is empty</h2>
          <p>Add some items to your cart before proceeding to checkout.</p>
          <button onClick={onCancel} className="btn btn-primary">
            Continue Shopping
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="checkout-container">
      <div className="checkout-header">
        <h1>Checkout</h1>
        <button onClick={onCancel} className="btn btn-secondary">
          Back to Cart
        </button>
      </div>

      <div className="checkout-content">
        <div className="checkout-form">
          <h2>Shipping & Payment Information</h2>
          
          {error && <div className="error-message">{error}</div>}
          
          <form onSubmit={handleCheckout}>
            <div className="form-section">
              <h3>Shipping Address</h3>
              <textarea
                name="shippingAddress"
                value={formData.shippingAddress}
                onChange={handleInputChange}
                placeholder="Enter your full shipping address..."
                required
                rows={4}
                className="form-control"
              />
            </div>

            <div className="form-section">
              <h3>Billing Address</h3>
              <div className="checkbox-group">
                <label>
                  <input
                    type="checkbox"
                    onChange={handleSameAsBilling}
                  />
                  Same as shipping address
                </label>
              </div>
              <textarea
                name="billingAddress"
                value={formData.billingAddress}
                onChange={handleInputChange}
                placeholder="Enter your billing address..."
                required
                rows={4}
                className="form-control"
              />
            </div>

            <div className="form-section">
              <h3>Payment Method</h3>
              <select
                name="paymentMethod"
                value={formData.paymentMethod}
                onChange={handleInputChange}
                className="form-control"
                required
              >
                <option value="credit_card">Credit Card</option>
                <option value="debit_card">Debit Card</option>
                <option value="paypal">PayPal</option>
                <option value="bank_transfer">Bank Transfer</option>
                <option value="cash_on_delivery">Cash on Delivery</option>
              </select>
            </div>

            <div className="form-section">
              <h3>Order Notes (Optional)</h3>
              <textarea
                name="orderNotes"
                value={formData.orderNotes}
                onChange={handleInputChange}
                placeholder="Any special instructions for your order..."
                rows={3}
                className="form-control"
              />
            </div>

            <div className="checkout-actions">
              <button
                type="submit"
                disabled={processing}
                className="btn btn-success btn-lg"
              >
                {processing ? 'Processing...' : `Place Order - $${cart.totalAmount?.toFixed(2)}`}
              </button>
            </div>
          </form>
        </div>

        <div className="order-summary">
          <h2>Order Summary</h2>
          
          <div className="cart-items">
            {cart.cartItems.map(item => (
              <div key={item.cartItemId} className="cart-item">
                <div className="item-info">
                  <h4>{item.productName}</h4>
                  <p>{item.productDescription}</p>
                </div>
                <div className="item-details">
                  <span className="quantity">Qty: {item.quantity}</span>
                  <span className="price">${item.priceAtTimeOfAdd?.toFixed(2)}</span>
                  <span className="total">${(item.quantity * item.priceAtTimeOfAdd)?.toFixed(2)}</span>
                </div>
              </div>
            ))}
          </div>

          <div className="order-totals">
            <div className="total-row">
              <span>Items ({cart.totalItems}):</span>
              <span>${(cart.totalAmount?.toFixed(2) * 0.93)?.toFixed(2)}</span>
            </div>
            <div className="total-row">
              <span>Shipping:</span>
              <span>FREE</span>
            </div>
            <div className="total-row">
              <span>Tax (7%):</span>
              <span>${(cart.totalAmount * 0.07)?.toFixed(2)}</span>
            </div>
            <div className="total-row grand-total">
              <span>Total:</span>
              <span>${cart.totalAmount?.toFixed(2)}</span>
            </div>
          </div>

          <div className="security-info">
            <p>🔒 Your payment information is secure and encrypted</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Checkout;
