import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:9080';

function App() {
  const [status, setStatus] = useState('Connecting...');
  const [error, setError] = useState(null);

  useEffect(() => {
    // Test connection to backend
    const testConnection = async () => {
      try {
        // Try to connect to actuator health endpoint (if available)
        // or just check if the server is responding
        await axios.get(`${API_BASE_URL}/actuator/health`, {
          timeout: 5000
        }).catch(() => {
          // If actuator is not available, try a simple request
          return axios.get(`${API_BASE_URL}`, { timeout: 5000 });
        });
        
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
        <p>Frontend Application</p>
      </div>
      
      <div className="container">
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
            <li>Create product management interface</li>
            <li>Build customer registration/login</li>
            <li>Implement shopping cart functionality</li>
            <li>Add seller dashboard</li>
            <li>Create marketplace stands management</li>
          </ul>
        </div>
      </div>
    </div>
  );
}

export default App;
