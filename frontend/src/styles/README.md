# CSS Structure Documentation

This project has been reorganized to separate CSS styles into component-specific files for better maintainability and organization.

## File Structure

```
src/
├── index.css                    # Main CSS file that imports all component styles
├── App.css                      # App-specific styles
└── styles/
    ├── global.css              # Global styles, base elements, and utility classes
    ├── Products.css            # Products component styles
    ├── Profile.css             # Shared profile component styles (Customer, Owner, Seller)
    ├── ProfileManager.css      # ProfileManager component styles
    └── ErrorBoundary.css       # ErrorBoundary component styles
```

## CSS Organization

### global.css
Contains:
- Base HTML element styles (body, code)
- Layout containers (.container, .header)
- Navigation styles (.nav-buttons, .nav-btn)
- Common UI components (.card, .btn, .btn-primary, .btn-secondary)
- Status message styles (.error, .success)

### Products.css
Contains all styles specific to the Products component:
- Product grid layout (.products-grid)
- Product card styling (.product-card, .product-header, .product-name, etc.)
- Stock and seller information display
- Product action buttons

### Profile.css
Contains shared styles used by profile components (CustomerProfile, OwnerProfile, SellerProfile):
- Profile layout (.profile-header, .profile-view)
- Form elements (.profile-form, .form-group, .form-input, etc.)
- Information display grids (.info-grid, .info-item)
- Business summary cards and address display
- Stock and stand management styles

### ProfileManager.css
Contains styles specific to the ProfileManager component:
- Profile selector interface (.profile-selector, .selector-group)
- Profile type buttons (.profile-type-btn)
- Profile selection dropdown (.profile-select)
- No profiles state display (.no-profiles)

### ErrorBoundary.css
Contains styles for error display and handling:
- Error boundary container (.error-boundary)
- Error message formatting
- Stack trace display

## Import Structure

The main `index.css` file imports all component-specific CSS files using `@import` statements. This ensures all styles are available throughout the application while maintaining organization.

## Benefits

1. **Maintainability**: Styles are organized by component, making it easier to find and modify specific styles
2. **Modularity**: Each component's styles are self-contained and clearly defined
3. **Reusability**: Shared styles are grouped in logical files (like Profile.css for all profile components)
4. **Performance**: CSS is still bundled efficiently through the import system
5. **Developer Experience**: Easier to understand which styles apply to which components

## Usage

When working on a specific component:
1. Look for component-specific styles in the corresponding CSS file in the `styles/` directory
2. For shared utilities and base styles, check `global.css`
3. All styles remain available globally, so existing class names continue to work as expected
