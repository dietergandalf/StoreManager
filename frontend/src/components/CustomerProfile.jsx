import React, { useState, useEffect } from 'react';
import { customerApi } from '../api';

function CustomerProfile({ customerId }) {
  const [customer, setCustomer] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [editForm, setEditForm] = useState({});

  useEffect(() => {
    if (customerId) {
      fetchCustomer();
    }
  }, [customerId]);

  const fetchCustomer = async () => {
    try {
      setLoading(true);
      setError(null);
      const customerData = await customerApi.getCustomerById(customerId);
      setCustomer(customerData);
      setEditForm(customerData);
    } catch (err) {
      console.error('Error fetching customer:', err);
      setError(`Failed to load customer profile: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = () => {
    setIsEditing(true);
    setEditForm({ ...customer });
  };

  const handleCancel = () => {
    setIsEditing(false);
    setEditForm({ ...customer });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    if (name.startsWith('address.')) {
      const addressField = name.split('.')[1];
      setEditForm(prev => ({
        ...prev,
        address: {
          ...prev.address,
          [addressField]: value
        }
      }));
    } else {
      setEditForm(prev => ({
        ...prev,
        [name]: value
      }));
    }
  };

  const handleSave = async () => {
    try {
      setError(null);
      const updateData = {
        firstName: editForm.firstName,
        lastName: editForm.lastName,
        dateOfBirth: editForm.dateOfBirth,
        phoneNumber: editForm.phoneNumber,
        address: editForm.address
      };
      
      const updatedCustomer = await customerApi.updateCustomer(customerId, updateData);
      setCustomer({ ...editForm, ...updatedCustomer });
      setIsEditing(false);
    } catch (err) {
      console.error('Error updating customer:', err);
      setError(`Failed to update profile: ${err.message}`);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'Not specified';
    return new Date(dateString).toLocaleDateString();
  };

  if (loading) {
    return (
      <div className="card">
        <h2>👤 Customer Profile</h2>
        <p>Loading customer profile...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="card">
        <h2>👤 Customer Profile</h2>
        <div className="error">
          {error}
        </div>
        <button className="btn" onClick={fetchCustomer}>
          Try Again
        </button>
      </div>
    );
  }

  if (!customer) {
    return (
      <div className="card">
        <h2>👤 Customer Profile</h2>
        <p>Customer not found.</p>
      </div>
    );
  }

  return (
    <div className="card">
      <div className="profile-header">
        <h2>👤 Customer Profile</h2>
        {!isEditing && (
          <button className="btn btn-secondary" onClick={handleEdit}>
            ✏️ Edit Profile
          </button>
        )}
      </div>

      {isEditing ? (
        <div className="profile-form">
          <div className="form-group">
            <label>First Name:</label>
            <input
              type="text"
              name="firstName"
              value={editForm.firstName || ''}
              onChange={handleInputChange}
              className="form-input"
            />
          </div>

          <div className="form-group">
            <label>Last Name:</label>
            <input
              type="text"
              name="lastName"
              value={editForm.lastName || ''}
              onChange={handleInputChange}
              className="form-input"
            />
          </div>

          <div className="form-group">
            <label>Email:</label>
            <input
              type="email"
              value={editForm.email || ''}
              disabled
              className="form-input disabled"
              title="Email cannot be changed"
            />
          </div>

          <div className="form-group">
            <label>Phone Number:</label>
            <input
              type="tel"
              name="phoneNumber"
              value={editForm.phoneNumber || ''}
              onChange={handleInputChange}
              className="form-input"
            />
          </div>

          <div className="form-group">
            <label>Date of Birth:</label>
            <input
              type="date"
              name="dateOfBirth"
              value={editForm.dateOfBirth || ''}
              onChange={handleInputChange}
              className="form-input"
            />
          </div>

          <h3>Address</h3>
          <div className="form-group">
            <label>Street:</label>
            <input
              type="text"
              name="address.street"
              value={editForm.address?.street || ''}
              onChange={handleInputChange}
              className="form-input"
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>City:</label>
              <input
                type="text"
                name="address.city"
                value={editForm.address?.city || ''}
                onChange={handleInputChange}
                className="form-input"
              />
            </div>

            <div className="form-group">
              <label>Postal Code:</label>
              <input
                type="text"
                name="address.postalCode"
                value={editForm.address?.postalCode || ''}
                onChange={handleInputChange}
                className="form-input"
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Province:</label>
              <input
                type="text"
                name="address.province"
                value={editForm.address?.province || ''}
                onChange={handleInputChange}
                className="form-input"
              />
            </div>

            <div className="form-group">
              <label>Country:</label>
              <input
                type="text"
                name="address.country"
                value={editForm.address?.country || ''}
                onChange={handleInputChange}
                className="form-input"
              />
            </div>
          </div>

          <div className="form-actions">
            <button className="btn btn-primary" onClick={handleSave}>
              💾 Save Changes
            </button>
            <button className="btn btn-secondary" onClick={handleCancel}>
              ❌ Cancel
            </button>
          </div>
        </div>
      ) : (
        <div className="profile-view">
          <div className="profile-section">
            <h3>Personal Information</h3>
            <div className="info-grid">
              <div className="info-item">
                <span className="label">Name:</span>
                <span className="value">{customer.firstName} {customer.lastName}</span>
              </div>
              <div className="info-item">
                <span className="label">Email:</span>
                <span className="value">{customer.email}</span>
              </div>
              <div className="info-item">
                <span className="label">Phone:</span>
                <span className="value">{customer.phoneNumber || 'Not specified'}</span>
              </div>
              <div className="info-item">
                <span className="label">Date of Birth:</span>
                <span className="value">{formatDate(customer.dateOfBirth)}</span>
              </div>
            </div>
          </div>

          {customer.address && (
            <div className="profile-section">
              <h3>Address</h3>
              <div className="address-display">
                <p>{customer.address.street}</p>
                <p>{customer.address.city}, {customer.address.province} {customer.address.postalCode}</p>
                <p>{customer.address.country}</p>
              </div>
            </div>
          )}

          <div className="profile-section">
            <h3>Shopping Information</h3>
            <div className="info-grid">
              <div className="info-item">
                <span className="label">Customer ID:</span>
                <span className="value">{customer.personId}</span>
              </div>
              <div className="info-item">
                <span className="label">Cart ID:</span>
                <span className="value">{customer.cartId || 'No active cart'}</span>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default CustomerProfile;
