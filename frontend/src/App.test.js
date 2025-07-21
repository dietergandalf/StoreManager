import { render, screen } from '@testing-library/react';
import App from './App';

test('renders store manager app', () => {
  render(<App />);
  const linkElement = screen.getByText(/store manager/i);
  expect(linkElement).toBeInTheDocument();
});

test('app component exists', () => {
  expect(App).toBeDefined();
});
