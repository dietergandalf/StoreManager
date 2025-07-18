import React, { useState, useEffect } from 'react';
import { customerApi } from '../api/storeApi';
import '../styles/OrderHistory.css';

const OrderHistory = ({ customerId }) => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadOrders();
  }, [customerId]);

  const loadOrders = async () => {
    try {
      setLoading(true);
      const ordersData = await customerApi.getOrders(customerId);
      setOrders(ordersData);
    } catch (error) {
      console.error('Error loading orders:', error);
      setError('Failed to load order history. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'PENDING': return '#ffc107';
      case 'CONFIRMED': return '#007bff';
      case 'PROCESSING': return '#fd7e14';
      case 'SHIPPED': return '#6f42c1';
      case 'DELIVERED': return '#28a745';
      case 'CANCELLED': return '#dc3545';
      case 'REFUNDED': return '#6c757d';
      default: return '#6c757d';
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  if (loading) {
    return (
      <div className="order-history-container">
        <div className="loading">Loading order history...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="order-history-container">
        <div className="error-message">{error}</div>
        <button onClick={loadOrders} className="btn btn-primary">
          Try Again
        </button>
      </div>
    );
  }

  if (orders.length === 0) {
    return (
      <div className="order-history-container">
        <div className="no-orders">
          <h2>No Orders Yet</h2>
          <p>You haven't placed any orders yet. Start shopping to see your order history here!</p>
        </div>
      </div>
    );
  }

  return (
    <div className="order-history-container">
      <div className="order-history-header">
        <h1>Order History</h1>
        <p>View and track all your past orders</p>
      </div>

      <div className="orders-list">
        {orders.map(order => (
          <div key={order.orderId} className="order-card">
            <div className="order-header">
              <div className="order-info">
                <h3>Order #{order.orderId}</h3>
                <p className="order-date">{formatDate(order.orderDate)}</p>
              </div>
              <div className="order-status">
                <span 
                  className="status-badge" 
                  style={{ backgroundColor: getStatusColor(order.status) }}
                >
                  {order.status}
                </span>
              </div>
            </div>

            <div className="order-details">
              <div className="order-items">
                <h4>Items ({order.orderItems?.length || 0}):</h4>
                {order.orderItems?.map(item => (
                  <div key={item.orderItemId} className="order-item">
                    <div className="item-info">
                      <span className="item-name">{item.productName}</span>
                      <span className="item-quantity">Qty: {item.quantity}</span>
                    </div>
                    <div className="item-price">
                      ${item.totalPrice?.toFixed(2)}
                    </div>
                  </div>
                ))}
              </div>

              <div className="order-summary">
                <div className="summary-row">
                  <span>Payment Method:</span>
                  <span>{order.paymentMethod?.replace('_', ' ').toUpperCase()}</span>
                </div>
                <div className="summary-row">
                  <span>Payment Status:</span>
                  <span className={`payment-status ${order.paymentStatus?.toLowerCase()}`}>
                    {order.paymentStatus}
                  </span>
                </div>
                <div className="summary-row total">
                  <span>Total Amount:</span>
                  <span>${order.totalAmount?.toFixed(2)}</span>
                </div>
              </div>
            </div>

            {order.shippingAddress && (
              <div className="order-addresses">
                <div className="address-section">
                  <h5>Shipping Address:</h5>
                  <p>{order.shippingAddress}</p>
                </div>
              </div>
            )}

            {order.orderNotes && (
              <div className="order-notes">
                <h5>Order Notes:</h5>
                <p>{order.orderNotes}</p>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default OrderHistory;
