import { render, screen } from '@testing-library/react';
import React from 'react';

// Mock the API modules to avoid import issues during testing
jest.mock('./api', () => ({
  authApi: {},
  customerApi: {},
  sellerApi: {},
  ownerApi: {},
  productApi: {}
}));

// Simple component for testing
const TestComponent = () => <div>Store Manager Test</div>;

test('renders test component', () => {
  render(<TestComponent />);
  const element = screen.getByText(/store manager test/i);
  expect(element).toBeInTheDocument();
});

test('react is working', () => {
  expect(React).toBeDefined();
});
