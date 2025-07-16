import React, { useState, useEffect, useCallback } from 'react';
import { sellerApi } from '../api';

function SellerProfile({ sellerId }) {
  const [seller, setSeller] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [editForm, setEditForm] = useState({});

  const fetchSeller = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const sellerData = await sellerApi.getSellerById(sellerId);
      setSeller(sellerData);
      setEditForm(sellerData);
    } catch (err) {
      console.error('Error fetching seller:', err);
      setError(`Failed to load seller profile: ${err.message}`);
    } finally {
      setLoading(false);
    }
  }, [sellerId]);

  useEffect(() => {
    if (sellerId) {
      fetchSeller();
    }
  }, [sellerId, fetchSeller]);

  const handleEdit = () => {
    setIsEditing(true);
    setEditForm({ ...seller });
  };

  const handleCancel = () => {
    setIsEditing(false);
    setEditForm({ ...seller });
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
      
      const updatedSeller = await sellerApi.updateSeller(sellerId, updateData);
      setSeller({ ...editForm, ...updatedSeller });
      setIsEditing(false);
    } catch (err) {
      console.error('Error updating seller:', err);
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
        <h2>🏪 Seller Profile</h2>
        <p>Loading seller profile...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="card">
        <h2>🏪 Seller Profile</h2>
        <div className="error">
          {error}
        </div>
        <button className="btn" onClick={fetchSeller}>
          Try Again
        </button>
      </div>
    );
  }

  if (!seller) {
    return (
      <div className="card">
        <h2>🏪 Seller Profile</h2>
        <p>Seller not found.</p>
      </div>
    );
  }

  return (
    <div className="card">
      <div className="profile-header">
        <h2>🏪 Seller Profile</h2>
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
                <span className="value">{seller.firstName} {seller.lastName}</span>
              </div>
              <div className="info-item">
                <span className="label">Email:</span>
                <span className="value">{seller.email}</span>
              </div>
              <div className="info-item">
                <span className="label">Phone:</span>
                <span className="value">{seller.phoneNumber || 'Not specified'}</span>
              </div>
              <div className="info-item">
                <span className="label">Date of Birth:</span>
                <span className="value">{formatDate(seller.dateOfBirth)}</span>
              </div>
            </div>
          </div>

          {seller.address && (
            <div className="profile-section">
              <h3>Address</h3>
              <div className="address-display">
                <p>{seller.address.street}</p>
                <p>{seller.address.city}, {seller.address.province} {seller.address.postalCode}</p>
                <p>{seller.address.country}</p>
              </div>
            </div>
          )}

          <div className="profile-section">
            <h3>Business Information</h3>
            <div className="info-grid">
              <div className="info-item">
                <span className="label">Seller ID:</span>
                <span className="value">{seller.personId}</span>
              </div>
              <div className="info-item">
                <span className="label">Stand ID:</span>
                <span className="value">{seller.standId || 'No stand assigned'}</span>
              </div>
              <div className="info-item">
                <span className="label">Products in Stock:</span>
                <span className="value">
                  {seller.productStockIds ? seller.productStockIds.length : 0} products
                </span>
              </div>
            </div>
          </div>

          {seller.productStockIds && seller.productStockIds.length > 0 && (
            <div className="profile-section">
              <h3>Product Stock IDs</h3>
              <div className="product-stock-list">
                {seller.productStockIds.map((stockId, index) => (
                  <span key={stockId} className="stock-id-badge">
                    #{stockId}
                  </span>
                ))}
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default SellerProfile;
