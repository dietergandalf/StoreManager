import apiClient from './apiClient';

// Authentication API calls
export const authApi = {
  // Register a new user (customer, seller, or owner)
  register: async (registerData) => {
    let endpoint;
    const { userType, ...userData } = registerData;
    
    switch (userType) {
      case 'customer':
        endpoint = '/api/customers';
        break;
      case 'seller':
        endpoint = '/api/sellers';
        break;
      case 'owner':
        endpoint = '/api/owners';
        break;
      default:
        throw new Error('Invalid user type');
    }
    
    const response = await apiClient.post(endpoint, userData);
    return { ...response.data, userType };
  },

  // Login user by checking existence and returning user data
  login: async (loginData) => {
    const { email, password } = loginData;
    
    // Since we don't have proper authentication yet, we'll check each endpoint
    // In a real application, this would be a single login endpoint
    const userTypes = ['customers', 'sellers', 'owners'];
    
    for (const userType of userTypes) {
      try {
        const response = await apiClient.get(`/api/${userType}`);
        const users = response.data;
        
        // Find user by email (password check would be done on backend in real app)
        const user = users.find(u => u.email === email);
        if (user) {
          // Remove the 's' from userType for consistency
          const cleanUserType = userType.slice(0, -1);
          console.log('Found user:', user);
          return { 
            ...user, 
            userType: cleanUserType,
            // In a real app, you'd get a JWT token from the backend
            token: `mock-jwt-token-${user.personId}`,
            userId: user.personId // Make sure we use the correct ID
          };
        }
      } catch (error) {
        console.warn(`Failed to check ${userType}:`, error.message);
      }
    }
    
    throw new Error('Invalid email or password');
  },

  // Logout (mainly for clearing frontend state)
  logout: () => {
    // In a real app, you might call an endpoint to invalidate the token
    localStorage.removeItem('authToken');
    localStorage.removeItem('userType');
    localStorage.removeItem('userId');
    return Promise.resolve();
  },

  // Get current user data from localStorage
  getCurrentUser: () => {
    const token = localStorage.getItem('authToken');
    const userType = localStorage.getItem('userType');
    const userId = localStorage.getItem('userId');
    
    if (token && userType && userId) {
      return { token, userType, userId };
    }
    return null;
  },

  // Store authentication data
  setAuthData: (user) => {
    console.log('Storing auth data:', user);
    localStorage.setItem('authToken', user.token);
    localStorage.setItem('userType', user.userType);
    localStorage.setItem('userId', (user.userId || user.personId).toString());
    localStorage.setItem('userEmail', user.email);
    localStorage.setItem('userName', `${user.firstName} ${user.lastName}`);
  }
};
