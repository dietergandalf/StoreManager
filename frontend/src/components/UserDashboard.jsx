import React, { useState, useEffect, useCallback } from 'react';
import { authApi, customerApi, sellerApi, ownerApi } from '../api';
import CustomerProfile from './CustomerProfile';
import SellerProfile from './SellerProfile';
import OwnerProfile from './OwnerProfile';
import Products from './Products';
import ShoppingCart from './ShoppingCart';
import OrderHistory from './OrderHistory';
import './UserDashboard.css';

const UserDashboard = ({ user, onLogout }) => {
  const [activeTab, setActiveTab] = useState('profile');
  const [userDetails, setUserDetails] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadUserDetails = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      let details;
      
      console.log('Loading user details for:', { userType: user.userType, userId: user.userId });
      
      switch (user.userType) {
        case 'customer':
          details = await customerApi.getCustomerById(user.userId);
          break;
        case 'seller':
          details = await sellerApi.getSellerById(user.userId);
          break;
        case 'owner':
          details = await ownerApi.getOwnerById(user.userId);
          break;
        default:
          throw new Error('Unknown user type');
      }
      
      setUserDetails(details);
    } catch (err) {
      console.error('Error loading user details:', err);
      
      // If individual user fetch fails, try to get user from the list
      try {
        console.log('Trying to fetch from user list...');
        let allUsers;
        switch (user.userType) {
          case 'customer':
            allUsers = await customerApi.getAllCustomers();
            break;
          case 'seller':
            allUsers = await sellerApi.getAllSellers();
            break;
          case 'owner':
            allUsers = await ownerApi.getAllOwners();
            break;
          default:
            throw new Error('Unknown user type');
        }
        
        // Find user by ID or email
        const userDetail = allUsers.find(u => 
          u.personId?.toString() === user.userId?.toString() || 
          u.email === localStorage.getItem('userEmail')
        );
        
        if (userDetail) {
          setUserDetails(userDetail);
          // Update localStorage with correct ID if needed
          if (userDetail.personId.toString() !== user.userId) {
            localStorage.setItem('userId', userDetail.personId.toString());
          }
        } else {
          throw new Error('User not found in the system');
        }
      } catch (fallbackErr) {
        console.error('Fallback error:', fallbackErr);
        setError(`Error loading user details: ${err.response?.data?.message || err.message}`);
      }
    } finally {
      setLoading(false);
    }
  }, [user]);

  useEffect(() => {
    loadUserDetails();
  }, [loadUserDetails]);

  const handleLogout = async () => {
    try {
      await authApi.logout();
      onLogout();
    } catch (err) {
      console.error('Logout error:', err);
    }
  };

  const getUserTypeIcon = () => {
    switch (user.userType) {
      case 'customer':
        return '🛒';
      case 'seller':
        return '🏪';
      case 'owner':
        return '🏢';
      default:
        return '👤';
    }
  };

  const getUserTypeName = () => {
    return user.userType.charAt(0).toUpperCase() + user.userType.slice(1);
  };

  const getAvailableTabs = () => {
    const baseTabs = [
      { id: 'profile', label: 'Profile', icon: '👤' }
    ];

    switch (user.userType) {
      case 'customer':
        return [
          ...baseTabs,
          { id: 'products', label: 'Shop Products', icon: '🛍️' },
          { id: 'cart', label: 'Shopping Cart', icon: '🛒' },
          { id: 'orders', label: 'Order History', icon: '📋' }
        ];
      case 'seller':
        return [
          ...baseTabs,
          { id: 'inventory', label: 'My Products', icon: '📦' },
          { id: 'sales', label: 'Sales', icon: '💰' }
        ];
      case 'owner':
        return [
          ...baseTabs,
          { id: 'stands', label: 'My Stands', icon: '🏪' },
          { id: 'revenue', label: 'Revenue', icon: '📊' }
        ];
      default:
        return baseTabs;
    }
  };

  const renderTabContent = () => {
    if (loading) {
      return (
        <div className="loading-container">
          <div className="loading-spinner"></div>
          <p>Loading user details...</p>
        </div>
      );
    }

    if (error) {
      return (
        <div className="error-container">
          <p>Error loading user details: {error}</p>
          <button onClick={loadUserDetails} className="retry-button">
            Retry
          </button>
        </div>
      );
    }

    switch (activeTab) {
      case 'profile':
        if (user.userType === 'customer' && userDetails) {
          return <CustomerProfile customerId={userDetails.personId} />;
        } else if (user.userType === 'seller' && userDetails) {
          return <SellerProfile sellerId={userDetails.personId} />;
        } else if (user.userType === 'owner' && userDetails) {
          return <OwnerProfile ownerId={userDetails.personId} />;
        }
        return <div>Profile not available</div>;

      case 'products':
        return <Products />;

      case 'cart':
        return <ShoppingCart customerId={user.userId || user.id} />;

      case 'orders':
        return <OrderHistory customerId={user.userId || user.id} />;

      case 'inventory':
        return (
          <div className="tab-content">
            <h3>My Products</h3>
            <p>Product inventory management coming soon...</p>
          </div>
        );

      case 'sales':
        return (
          <div className="tab-content">
            <h3>Sales Dashboard</h3>
            <p>Sales analytics coming soon...</p>
          </div>
        );

      case 'stands':
        return (
          <div className="tab-content">
            <h3>My Stands</h3>
            <p>Stand management coming soon...</p>
          </div>
        );

      case 'revenue':
        return (
          <div className="tab-content">
            <h3>Revenue Dashboard</h3>
            <p>Revenue analytics coming soon...</p>
          </div>
        );

      default:
        return <div>Tab not found</div>;
    }
  };

  const availableTabs = getAvailableTabs();

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <div className="user-info">
          <span className="user-icon">{getUserTypeIcon()}</span>
          <div className="user-details">
            <h2>Welcome back!</h2>
            <p>
              {getUserTypeName()} Dashboard
              {userDetails && (
                <span className="user-email"> • {userDetails.email}</span>
              )}
            </p>
          </div>
        </div>
        <button onClick={handleLogout} className="logout-button">
          Logout
        </button>
      </div>

      <div className="dashboard-content">
        <nav className="dashboard-nav">
          {availableTabs.map((tab) => (
            <button
              key={tab.id}
              className={`nav-tab ${activeTab === tab.id ? 'active' : ''}`}
              onClick={() => setActiveTab(tab.id)}
            >
              <span className="tab-icon">{tab.icon}</span>
              {tab.label}
            </button>
          ))}
        </nav>

        <main className="dashboard-main">
          {renderTabContent()}
        </main>
      </div>
    </div>
  );
};

export default UserDashboard;
