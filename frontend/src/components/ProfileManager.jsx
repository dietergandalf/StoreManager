import React, { useState, useEffect } from 'react';
import { customerApi, sellerApi, ownerApi } from '../api';
import CustomerProfile from './CustomerProfile';
import SellerProfile from './SellerProfile';
import OwnerProfile from './OwnerProfile';

function ProfileManager() {
  const [profileType, setProfileType] = useState('customer');
  const [profileId, setProfileId] = useState('');
  const [availableProfiles, setAvailableProfiles] = useState({
    customers: [],
    sellers: [],
    owners: []
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchAvailableProfiles();
  }, []);

  const fetchAvailableProfiles = async () => {
    try {
      setLoading(true);
      setError(null);
      
      const [customersData, sellersData, ownersData] = await Promise.allSettled([
        customerApi.getAllCustomers(),
        sellerApi.getAllSellers(),
        ownerApi.getAllOwners()
      ]);

      setAvailableProfiles({
        customers: customersData.status === 'fulfilled' ? customersData.value : [],
        sellers: sellersData.status === 'fulfilled' ? sellersData.value : [],
        owners: ownersData.status === 'fulfilled' ? ownersData.value : []
      });
    } catch (err) {
      console.error('Error fetching profiles:', err);
      setError(`Failed to load available profiles: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  const handleProfileTypeChange = (type) => {
    setProfileType(type);
    setProfileId('');
  };

  const getCurrentProfiles = () => {
    switch (profileType) {
      case 'customer':
        return availableProfiles.customers;
      case 'seller':
        return availableProfiles.sellers;
      case 'owner':
        return availableProfiles.owners;
      default:
        return [];
    }
  };

  const renderProfile = () => {
    if (!profileId) return null;

    switch (profileType) {
      case 'customer':
        return <CustomerProfile customerId={profileId} />;
      case 'seller':
        return <SellerProfile sellerId={profileId} />;
      case 'owner':
        return <OwnerProfile ownerId={profileId} />;
      default:
        return null;
    }
  };

  return (
    <div>
      <div className="card">
        <h2>👥 Profile Manager</h2>
        
        {error && (
          <div className="error">
            {error}
            <button className="btn btn-secondary" onClick={fetchAvailableProfiles}>
              🔄 Retry
            </button>
          </div>
        )}

        <div className="profile-selector">
          <div className="selector-group">
            <label>Profile Type:</label>
            <div className="profile-type-buttons">
              <button
                className={`profile-type-btn ${profileType === 'customer' ? 'active' : ''}`}
                onClick={() => handleProfileTypeChange('customer')}
              >
                👤 Customer
              </button>
              <button
                className={`profile-type-btn ${profileType === 'seller' ? 'active' : ''}`}
                onClick={() => handleProfileTypeChange('seller')}
              >
                🏪 Seller
              </button>
              <button
                className={`profile-type-btn ${profileType === 'owner' ? 'active' : ''}`}
                onClick={() => handleProfileTypeChange('owner')}
              >
                🏢 Owner
              </button>
            </div>
          </div>

          <div className="selector-group">
            <label>Select Profile:</label>
            {loading ? (
              <p>Loading profiles...</p>
            ) : (
              <select
                value={profileId}
                onChange={(e) => setProfileId(e.target.value)}
                className="profile-select"
              >
                <option value="">-- Select a {profileType} --</option>
                {getCurrentProfiles().map((profile) => (
                  <option key={profile.personId} value={profile.personId}>
                    {profile.firstName} {profile.lastName} ({profile.email})
                  </option>
                ))}
              </select>
            )}
          </div>

          {getCurrentProfiles().length === 0 && !loading && (
            <div className="no-profiles">
              <p>No {profileType}s found in the system.</p>
              <p>You may need to create some {profileType} accounts first.</p>
            </div>
          )}
        </div>
      </div>

      {renderProfile()}
    </div>
  );
}

export default ProfileManager;
