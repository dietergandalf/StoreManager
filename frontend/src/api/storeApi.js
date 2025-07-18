import apiClient from './apiClient';

// Product API calls
export const productApi = {
  // Get all available products
  getAllProducts: async () => {
    const response = await apiClient.get('/api/customers/products');
    return response.data;
  },

  // Get product by ID (if needed in the future)
  getProductById: async (productId) => {
    const response = await apiClient.get(`/api/products/${productId}`);
    return response.data;
  },
};

// Customer API calls
export const customerApi = {
  // Get all customers
  getAllCustomers: async () => {
    const response = await apiClient.get('/api/customers');
    return response.data;
  },

  // Get customer by ID
  getCustomerById: async (customerId) => {
    const response = await apiClient.get(`/api/customers/${customerId}`);
    return response.data;
  },

  // Create new customer
  createCustomer: async (registerData) => {
    const response = await apiClient.post('/api/customers', registerData);
    return response.data;
  },

  // Update customer profile
  updateCustomer: async (customerId, updateData) => {
    const response = await apiClient.put(`/api/customers/${customerId}`, updateData);
    return response.data;
  },

  // Delete customer
  deleteCustomer: async (customerId) => {
    const response = await apiClient.delete(`/api/customers/${customerId}`);
    return response.data;
  },

  // Add product to cart
  addToCart: async (customerId, cartData) => {
    const response = await apiClient.post(`/api/customers/${customerId}/cart`, cartData);
    return response.data;
  },

  // Get customer's cart
  getCart: async (customerId) => {
    const response = await apiClient.get(`/api/customers/${customerId}/cart`);
    return response.data;
  },

  // Clear customer's cart
  clearCart: async (customerId) => {
    const response = await apiClient.delete(`/api/customers/${customerId}/cart`);
    return response.data;
  },

  // Remove item from cart
  removeFromCart: async (customerId, cartItemId) => {
    const response = await apiClient.delete(`/api/customers/${customerId}/cart/items/${cartItemId}`);
    return response.data;
  },

  // Update cart item quantity
  updateCartItemQuantity: async (customerId, cartItemId, quantity) => {
    const response = await apiClient.put(`/api/customers/${customerId}/cart/items/${cartItemId}?quantity=${quantity}`);
    return response.data;
  },

  // Checkout and create order
  checkout: async (customerId, checkoutData) => {
    const response = await apiClient.post(`/api/customers/${customerId}/checkout`, checkoutData);
    return response.data;
  },

  // Get customer orders
  getOrders: async (customerId) => {
    const response = await apiClient.get(`/api/customers/${customerId}/orders`);
    return response.data;
  },
};

// Order API calls
export const orderApi = {
  // Get order by ID
  getOrderById: async (orderId) => {
    const response = await apiClient.get(`/api/orders/${orderId}`);
    return response.data;
  },

  // Update order status
  updateOrderStatus: async (orderId, status) => {
    const response = await apiClient.put(`/api/orders/${orderId}/status?status=${status}`);
    return response.data;
  },

  // Get all orders (admin functionality)
  getAllOrders: async () => {
    const response = await apiClient.get('/api/orders');
    return response.data;
  },
};

// Seller API calls
export const sellerApi = {
  // Get all sellers
  getAllSellers: async () => {
    const response = await apiClient.get('/api/sellers');
    return response.data;
  },

  // Get seller by ID
  getSellerById: async (sellerId) => {
    const response = await apiClient.get(`/api/sellers/${sellerId}`);
    return response.data;
  },

  // Create new seller
  createSeller: async (registerData) => {
    const response = await apiClient.post('/api/sellers', registerData);
    return response.data;
  },

  // Update seller profile
  updateSeller: async (sellerId, updateData) => {
    const response = await apiClient.put(`/api/sellers/${sellerId}`, updateData);
    return response.data;
  },

  // Delete seller
  deleteSeller: async (sellerId) => {
    const response = await apiClient.delete(`/api/sellers/${sellerId}`);
    return response.data;
  },

  // Add product for seller
  addProduct: async (sellerId, productData) => {
    const response = await apiClient.post(`/api/sellers/${sellerId}/products`, productData);
    return response.data;
  },

  // Update product stock
  updateProductStock: async (sellerId, stockId, updateData) => {
    const response = await apiClient.put(`/api/sellers/${sellerId}/products/${stockId}`, updateData);
    return response.data;
  },

  // Get seller's products
  getSellerProducts: async (sellerId) => {
    const response = await apiClient.get(`/api/sellers/${sellerId}/products`);
    return response.data;
  },
};

// Owner API calls
export const ownerApi = {
  // Get all owners
  getAllOwners: async () => {
    const response = await apiClient.get('/api/owners');
    return response.data;
  },

  // Get owner by ID
  getOwnerById: async (ownerId) => {
    const response = await apiClient.get(`/api/owners/${ownerId}`);
    return response.data;
  },

  // Create new owner
  createOwner: async (registerData) => {
    const response = await apiClient.post('/api/owners', registerData);
    return response.data;
  },

  // Update owner profile
  updateOwner: async (ownerId, updateData) => {
    const response = await apiClient.put(`/api/owners/${ownerId}`, updateData);
    return response.data;
  },

  // Delete owner
  deleteOwner: async (ownerId) => {
    const response = await apiClient.delete(`/api/owners/${ownerId}`);
    return response.data;
  },
};

// System API calls
export const systemApi = {
  // Health check
  healthCheck: async () => {
    try {
      // Try actuator health endpoint first
      const response = await apiClient.get('/actuator/health');
      return response.data;
    } catch (error) {
      // Fallback to base endpoint
      await apiClient.get('/');
      return { status: 'UP' };
    }
  },
};
