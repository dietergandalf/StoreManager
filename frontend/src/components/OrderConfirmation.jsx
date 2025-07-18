import React from 'react';
import '../styles/OrderConfirmation.css';

const OrderConfirmation = ({ order, onContinueShopping, onViewOrders }) => {
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

  const getEstimatedDelivery = () => {
    const deliveryDate = new Date();
    deliveryDate.setDate(deliveryDate.getDate() + 3); // 3 days from now
    return deliveryDate.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  return (
    <div className="order-confirmation-container">
      <div className="confirmation-card">
        <div className="success-icon">
          <div className="checkmark">✓</div>
        </div>
        
        <h1>Order Placed Successfully!</h1>
        <p className="success-message">
          Thank you for your order. We've received your payment and will begin processing your items shortly.
        </p>

        <div className="order-details">
          <div className="detail-row">
            <span className="label">Order Number:</span>
            <span className="value">#{order.orderId}</span>
          </div>
          <div className="detail-row">
            <span className="label">Order Date:</span>
            <span className="value">{formatDate(order.orderDate)}</span>
          </div>
          <div className="detail-row">
            <span className="label">Total Amount:</span>
            <span className="value">${order.totalAmount?.toFixed(2)}</span>
          </div>
          <div className="detail-row">
            <span className="label">Payment Method:</span>
            <span className="value">{order.paymentMethod?.replace('_', ' ').toUpperCase()}</span>
          </div>
          <div className="detail-row">
            <span className="label">Payment Status:</span>
            <span className="value status-confirmed">{order.paymentStatus}</span>
          </div>
          <div className="detail-row">
            <span className="label">Order Status:</span>
            <span className="value status-processing">{order.status}</span>
          </div>
          <div className="detail-row">
            <span className="label">Estimated Delivery:</span>
            <span className="value">{getEstimatedDelivery()}</span>
          </div>
        </div>

        <div className="order-items">
          <h3>Items Ordered ({order.orderItems?.length || 0}):</h3>
          <div className="items-list">
            {order.orderItems?.map(item => (
              <div key={item.orderItemId} className="order-item">
                <div className="item-details">
                  <h4>{item.productName}</h4>
                  <p>{item.productDescription}</p>
                  <div className="item-meta">
                    <span>Quantity: {item.quantity}</span>
                    <span>Price: ${item.priceAtTimeOfOrder?.toFixed(2)}</span>
                    <span className="item-total">Total: ${item.totalPrice?.toFixed(2)}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="shipping-info">
          <h3>Shipping Information:</h3>
          <div className="address-block">
            <p>{order.shippingAddress}</p>
          </div>
        </div>

        <div className="what-happens-next">
          <h3>What happens next?</h3>
          <div className="timeline">
            <div className="timeline-item completed">
              <div className="timeline-icon">✓</div>
              <div className="timeline-content">
                <h4>Order Confirmed</h4>
                <p>Your order has been received and payment confirmed</p>
              </div>
            </div>
            <div className="timeline-item">
              <div className="timeline-icon">📦</div>
              <div className="timeline-content">
                <h4>Processing</h4>
                <p>We're preparing your items for shipment</p>
              </div>
            </div>
            <div className="timeline-item">
              <div className="timeline-icon">🚚</div>
              <div className="timeline-content">
                <h4>Shipped</h4>
                <p>Your order will be shipped within 1-2 business days</p>
              </div>
            </div>
            <div className="timeline-item">
              <div className="timeline-icon">🏠</div>
              <div className="timeline-content">
                <h4>Delivered</h4>
                <p>Estimated delivery: {getEstimatedDelivery()}</p>
              </div>
            </div>
          </div>
        </div>

        <div className="confirmation-actions">
          <button 
            onClick={onContinueShopping}
            className="btn btn-primary btn-lg"
          >
            Continue Shopping
          </button>
          <button 
            onClick={onViewOrders}
            className="btn btn-secondary btn-lg"
          >
            View All Orders
          </button>
        </div>

        <div className="contact-info">
          <p>
            Questions about your order? Contact us at{' '}
            <a href="mailto:support@storemanager.com">support@storemanager.com</a>
            {' '}or call (555) 123-4567
          </p>
        </div>
      </div>
    </div>
  );
};

export default OrderConfirmation;
