# StoreManager
A showcase store product build with spring boot, docker, maven, PostgreSQL and react.


Made changes.

Made changes.

Made changes.

Made changes.

Made changes.

Made changes.

Made changes.

Now let me create a simplified DTO for the ProductStock with minimal product information to avoid circular references: 

Made changes.

Now let me create a summary of all the DTOs I've created: Perfect! I've successfully created comprehensive DTOs for your Store Manager application. Here's a summary of what I've created:

## Core Entity DTOs:
1. **AddressDto** - For address information
2. **PersonDto** - Base DTO for person entities (excludes password for security)
3. **CustomerDto** - Extends PersonDto with cart reference
4. **OwnerDto** - Extends PersonDto with stand information and business logic fields
5. **SellerDto** - Extends PersonDto with product stock and stand references
6. **ProductDto** - For product information
7. **ProductStockDto** - For product inventory with seller and product details
8. **ProductStockSummaryDto** - Simplified version to avoid circular references
9. **StandDto** - For stand information with owner and user details
10. **ShoppingCartDto** - For shopping cart with calculated totals
11. **CreditCardPaymentDto** - For credit card payment information
12. **PayPalPaymentDto** - For PayPal payment information

## Request DTOs:
1. **LoginRequestDto** - For user authentication
2. **RegisterRequestDto** - For user registration with user type
3. **UpdateProfileRequestDto** - For profile updates
4. **ChangePasswordRequestDto** - For password changes
5. **AddToCartRequestDto** - For adding items to cart
6. **CreateProductRequestDto** - For creating new products
7. **CreateStandRequestDto** - For creating new stands

## Utility DTOs:
1. **ApiResponseDto** - Generic response wrapper with success/error handling and static helper methods

## Key Features of the DTOs:

### Security Considerations:
- Password fields are excluded from DTOs to prevent accidental exposure
- Credit card numbers should be masked in real implementation
- Sensitive information is properly separated

### Performance Optimization:
- Foreign key relationships are represented as IDs with optional names
- Circular references are avoided
- Summary DTOs are provided for lightweight operations

### Flexibility:
- Builder pattern for easy object creation
- Calculated fields (like totalAmount, totalItems) for convenience
- Generic response wrapper for consistent API responses

### Best Practices:
- All DTOs use Lombok annotations for cleaner code
- Consistent naming conventions
- Proper inheritance hierarchy for Person-related DTOs
- Separation of concerns between request and response DTOs

These DTOs provide a clean layer between your REST APIs and your domain models, allowing you to:
- Control what data is exposed to clients
- Handle different representations of the same data
- Avoid circular references in JSON serialization
- Implement proper validation and transformation logic
- Maintain API stability while evolving your domain models

You can now use these DTOs in your controllers and services to create a robust and secure API layer for your Store Manager application.