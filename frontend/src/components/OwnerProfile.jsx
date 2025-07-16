import React, { useState, useEffect, useCallback } from 'react';
import { ownerApi } from '../api';

function OwnerProfile({ ownerId }) {
  const [owner, setOwner] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [editForm, setEditForm] = useState({});

  const fetchOwner = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const ownerData = await ownerApi.getOwnerById(ownerId);
      setOwner(ownerData);
      setEditForm(ownerData);
    } catch (err) {
      console.error('Error fetching owner:', err);
      setError(`Failed to load owner profile: ${err.message}`);
    } finally {
      setLoading(false);
    }
  }, [ownerId]);

  useEffect(() => {
    if (ownerId) {
      fetchOwner();
    }
  }, [ownerId, fetchOwner]);

  const handleEdit = () => {
    setIsEditing(true);
    setEditForm({ ...owner });
  };

  const handleCancel = () => {
    setIsEditing(false);
    setEditForm({ ...owner });
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
      
      const updatedOwner = await ownerApi.updateOwner(ownerId, updateData);
      setOwner({ ...editForm, ...updatedOwner });
      setIsEditing(false);
    } catch (err) {
      console.error('Error updating owner:', err);
      setError(`Failed to update profile: ${err.message}`);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'Not specified';
    return new Date(dateString).toLocaleDateString();
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  };

  if (loading) {
    return (
      <div className="card">
        <h2>🏢 Store Owner Profile</h2>
        <p>Loading owner profile...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="card">
        <h2>🏢 Store Owner Profile</h2>
        <div className="error">
          {error}
        </div>
        <button className="btn" onClick={fetchOwner}>
          Try Again
        </button>
      </div>
    );
  }

  if (!owner) {
    return (
      <div className="card">
        <h2>🏢 Store Owner Profile</h2>
        <p>Owner not found.</p>
      </div>
    );
  }

  return (
    <div className="card">
      <div className="profile-header">
        <h2>🏢 Store Owner Profile</h2>
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
                <span className="value">{owner.firstName} {owner.lastName}</span>
              </div>
              <div className="info-item">
                <span className="label">Email:</span>
                <span className="value">{owner.email}</span>
              </div>
              <div className="info-item">
                <span className="label">Phone:</span>
                <span className="value">{owner.phoneNumber || 'Not specified'}</span>
              </div>
              <div className="info-item">
                <span className="label">Date of Birth:</span>
                <span className="value">{formatDate(owner.dateOfBirth)}</span>
              </div>
            </div>
          </div>

          {owner.address && (
            <div className="profile-section">
              <h3>Address</h3>
              <div className="address-display">
                <p>{owner.address.street}</p>
                <p>{owner.address.city}, {owner.address.province} {owner.address.postalCode}</p>
                <p>{owner.address.country}</p>
              </div>
            </div>
          )}

          <div className="profile-section">
            <h3>Business Information</h3>
            <div className="info-grid">
              <div className="info-item">
                <span className="label">Owner ID:</span>
                <span className="value">{owner.personId}</span>
              </div>
              <div className="info-item">
                <span className="label">Total Rent Income:</span>
                <span className="value rent-amount">{formatCurrency(owner.totalRent || 0)}</span>
              </div>
              <div className="info-item">
                <span className="label">Number of Stands:</span>
                <span className="value">{owner.standIds ? owner.standIds.length : 0} stands</span>
              </div>
              <div className="info-item">
                <span className="label">Available Stands:</span>
                <span className={`value ${owner.hasAvailableStands ? 'positive' : 'negative'}`}>
                  {owner.hasAvailableStands ? '✅ Yes' : '❌ No'}
                </span>
              </div>
            </div>
          </div>

          {owner.standIds && owner.standIds.length > 0 && (
            <div className="profile-section">
              <h3>Owned Stands</h3>
              <div className="stands-list">
                {owner.standIds.map((standId, index) => (
                  <div key={standId} className="stand-item">
                    <span className="stand-id">Stand #{standId}</span>
                  </div>
                ))}
              </div>
            </div>
          )}

          <div className="profile-section">
            <h3>Business Summary</h3>
            <div className="business-summary">
              <div className="summary-card">
                <h4>Revenue Overview</h4>
                <p className="summary-text">
                  Total rental income from {owner.standIds ? owner.standIds.length : 0} stands: 
                  <strong> {formatCurrency(owner.totalRent || 0)}</strong>
                </p>
              </div>
              
              <div className="summary-card">
                <h4>Capacity Status</h4>
                <p className="summary-text">
                  {owner.hasAvailableStands 
                    ? "You have available stands for new sellers." 
                    : "All stands are currently occupied."}
                </p>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default OwnerProfile;
