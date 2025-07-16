import React, { useState, useEffect } from 'react';
import { systemApi } from './api';
import Products from './components/Products';
import ProfileManager from './components/ProfileManager';
import ErrorBoundary from './components/ErrorBoundary';
import './App.css';

function App() {
  const [status, setStatus] = useState('Connecting...');
  const [error, setError] = useState(null);
  const [currentView, setCurrentView] = useState('home');

  useEffect(() => {
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

    testConnection();
  }, []);

  return (
    <div className="App">
      <div className="header">
        <h1>🏪 Store Manager</h1>
        <p>Test Application</p>
        
        <nav className="nav-buttons">
          <button 
            className={`nav-btn ${currentView === 'home' ? 'active' : ''}`}
            onClick={() => setCurrentView('home')}
          >
            Home
          </button>
          <button 
            className={`nav-btn ${currentView === 'products' ? 'active' : ''}`}
            onClick={() => setCurrentView('products')}
          >
            Products
          </button>
          <button 
            className={`nav-btn ${currentView === 'profiles' ? 'active' : ''}`}
            onClick={() => setCurrentView('profiles')}
          >
            Profiles
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
              <p>This is your frontend application running in Docker!</p>
              <ul>
                <li>✅ React Frontend: Running on port 3000</li>
                <li>✅ Spring Boot Backend: Running on port 8080</li>
                <li>✅ PostgreSQL Database: Running on port 15432</li>
                <li>✅ Docker Compose: All services orchestrated</li>
              </ul>
            </div>

            <div className="card">
              <h2>Next Steps</h2>
              <p>Now you can start building your Store Manager features:</p>
              <ul>
                <li>✅ Create product management interface</li>
                <li>✅ Create profile management (Customer, Seller, Owner)</li>
                <li>Build customer registration/login</li>
                <li>Implement shopping cart functionality</li>
                <li>Add seller dashboard</li>
                <li>Create marketplace stands management</li>
              </ul>
            </div>
          </>
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
