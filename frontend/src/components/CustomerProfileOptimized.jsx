import React, { useEffect } from 'react';
import { customerApi } from '../api';
import { useApi, useForm } from '../hooks';

function CustomerProfileOptimized({ customerId }) {
  const {
    data: customer,
    loading,
    error,
    execute: fetchCustomer,
    setError
  } = useApi(() => customerApi.getCustomerById(customerId), [customerId]);

  const {
    formData: editForm,
    handleChange,
    reset: resetForm
  } = useForm({});

  const [isEditing, setIsEditing] = React.useState(false);

  useEffect(() => {
    if (customerId) {
      fetchCustomer();
    }
  }, [customerId, fetchCustomer]);

  useEffect(() => {
    if (customer) {
      resetForm(customer);
    }
  }, [customer, resetForm]);

  const handleEdit = () => {
    setIsEditing(true);
    resetForm(customer);
  };

  const handleCancel = () => {
    setIsEditing(false);
    resetForm(customer);
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
      resetForm({ ...editForm, ...updatedCustomer });
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
        <h2>👤 Customer Profile (Optimized)</h2>
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
              onChange={handleChange}
              className="form-input"
            />
          </div>

          <div className="form-group">
            <label>Last Name:</label>
            <input
              type="text"
              name="lastName"
              value={editForm.lastName || ''}
              onChange={handleChange}
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
              onChange={handleChange}
              className="form-input"
            />
          </div>

          <div className="form-group">
            <label>Date of Birth:</label>
            <input
              type="date"
              name="dateOfBirth"
              value={editForm.dateOfBirth || ''}
              onChange={handleChange}
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
              onChange={handleChange}
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
                onChange={handleChange}
                className="form-input"
              />
            </div>

            <div className="form-group">
              <label>Postal Code:</label>
              <input
                type="text"
                name="address.postalCode"
                value={editForm.address?.postalCode || ''}
                onChange={handleChange}
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
                onChange={handleChange}
                className="form-input"
              />
            </div>

            <div className="form-group">
              <label>Country:</label>
              <input
                type="text"
                name="address.country"
                value={editForm.address?.country || ''}
                onChange={handleChange}
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

export default CustomerProfileOptimized;
