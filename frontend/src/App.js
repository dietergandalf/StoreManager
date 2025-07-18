import React, { useState, useEffect } from 'react';
import { systemApi, authApi } from './api';
import Products from './components/Products';
import ProfileManager from './components/ProfileManager';
import LoginRegister from './components/LoginRegister';
import UserDashboard from './components/UserDashboard';
import ErrorBoundary from './components/ErrorBoundary';
import './App.css';

function App() {
  const [status, setStatus] = useState('Connecting...');
  const [error, setError] = useState(null);
  const [currentView, setCurrentView] = useState('home');
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Check for existing authentication
    const checkAuth = () => {
      const currentUser = authApi.getCurrentUser();
      if (currentUser) {
        setUser(currentUser);
        setIsAuthenticated(true);
        setCurrentView('dashboard');
      }
      setIsLoading(false);
    };

    // Test connection to backend
    const testConnection = async () => {
      try {
        await systemApi.healthCheck();
        setStatus('Connected to backend successfully! 🎉');
        setError(null);
      } catch (err) {
        setStatus('Failed to connect to backend');
        setError(`Error: ${err.message}`);
      }
    };

    checkAuth();
    testConnection();
  }, []);

  const handleAuthSuccess = (userData) => {
    setUser(userData);
    setIsAuthenticated(true);
    setCurrentView('dashboard');
  };

  const handleLogout = () => {
    setUser(null);
    setIsAuthenticated(false);
    setCurrentView('home');
  };

  if (isLoading) {
    return (
      <div className="App">
        <div className="loading-container">
          <div className="loading-spinner"></div>
          <p>Loading...</p>
        </div>
      </div>
    );
  }

  // If user is authenticated, show dashboard
  if (isAuthenticated && user) {
    return (
      <div className="App">
        <ErrorBoundary>
          <UserDashboard user={user} onLogout={handleLogout} />
        </ErrorBoundary>
      </div>
    );
  }

  return (
    <div className="App">
      <div className="header">
        <h1>🏪 Store Manager</h1>
        <p>Marketplace Management System</p>
        
        <nav className="nav-buttons">
          <button 
            className={`nav-btn ${currentView === 'home' ? 'active' : ''}`}
            onClick={() => setCurrentView('home')}
          >
            Home
          </button>
          <button 
            className={`nav-btn ${currentView === 'login' ? 'active' : ''}`}
            onClick={() => setCurrentView('login')}
          >
            Login / Register
          </button>
          <button 
            className={`nav-btn ${currentView === 'products' ? 'active' : ''}`}
            onClick={() => setCurrentView('products')}
          >
            Browse Products
          </button>
          <button 
            className={`nav-btn ${currentView === 'profiles' ? 'active' : ''}`}
            onClick={() => setCurrentView('profiles')}
          >
            Profiles Demo
          </button>
        </nav>
      </div>
      
      <div className="container">
        {currentView === 'home' && (
          <>
            <div className="card">
              <h2>Backend Connection Status</h2>
              <p className={error ? 'error' : 'success'}>
                {status}
              </p>
              {error && (
                <div className="error">
                  {error}
                </div>
              )}
            </div>

            <div className="card">
              <h2>Welcome to Store Manager</h2>
              <p>A comprehensive marketplace management system with three user types:</p>
              <div className="user-types">
                <div className="user-type-card">
                  <span className="icon">🛒</span>
                  <h3>Customers</h3>
                  <p>Browse and purchase products from sellers in the marketplace</p>
                </div>
                <div className="user-type-card">
                  <span className="icon">🏪</span>
                  <h3>Sellers</h3>
                  <p>Sell products and manage inventory at marketplace stands</p>
                </div>
                <div className="user-type-card">
                  <span className="icon">🏢</span>
                  <h3>Owners</h3>
                  <p>Own marketplace stands and manage sellers, collect rent</p>
                </div>
              </div>
              <div className="cta">
                <button 
                  className="cta-button"
                  onClick={() => setCurrentView('login')}
                >
                  Get Started - Login or Register
                </button>
              </div>
            </div>

            <div className="card">
              <h2>System Status</h2>
              <ul>
                <li>✅ React Frontend: Running on port 3000</li>
                <li>✅ Spring Boot Backend: Running on port 8080</li>
                <li>✅ PostgreSQL Database: Running on port 15432</li>
                <li>✅ Docker Compose: All services orchestrated</li>
                <li>✅ User Authentication: Login/Register system</li>
                <li>✅ Multi-user Support: Customer, Seller, Owner roles</li>
              </ul>
            </div>
          </>
        )}

        {currentView === 'login' && (
          <ErrorBoundary>
            <LoginRegister onAuthSuccess={handleAuthSuccess} />
          </ErrorBoundary>
        )}
        
        {currentView === 'products' && (
          <ErrorBoundary>
            <Products />
          </ErrorBoundary>
        )}
        
        {currentView === 'profiles' && (
          <ErrorBoundary>
            <ProfileManager />
          </ErrorBoundary>
        )}
      </div>
    </div>
  );
}

export default App;
