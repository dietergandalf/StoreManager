import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import React from 'react';
import ErrorBoundary from './components/ErrorBoundary';
import Products from './components/Products';

// Mock the API modules to avoid import issues during testing
jest.mock('./api', () => ({
  authApi: {},
  customerApi: {},
  sellerApi: {},
  ownerApi: {},
  productApi: {
    getProducts: jest.fn(() => Promise.resolve({ data: [] }))
  }
}));

// Wrapper component for router-dependent components
const TestWrapper = ({ children }) => (
  <BrowserRouter>
    {children}
  </BrowserRouter>
);

test('ErrorBoundary renders children when there is no error', () => {
  const TestChild = () => <div>Test Child Component</div>;
  
  render(
    <ErrorBoundary>
      <TestChild />
    </ErrorBoundary>
  );
  
  expect(screen.getByText('Test Child Component')).toBeInTheDocument();
});

test('Products component renders without crashing', () => {
  render(
    <TestWrapper>
      <Products />
    </TestWrapper>
  );
  
  // Products component should render with card class
  expect(screen.getByText('🛍️ Available Products')).toBeInTheDocument();
});

test('React is properly configured', () => {
  expect(React).toBeDefined();
  expect(React.version).toBeDefined();
});
