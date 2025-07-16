# Store Manager Backend Tests

This directory contains comprehensive unit tests, integration tests, and performance tests for the Store Manager backend application.

## Test Structure

### Unit Tests
- **Service Tests**: Test business logic in isolation using mocked dependencies
  - `CustomerServiceTest.java`
  - `SellerServiceTest.java`
  - `OwnerServiceTest.java`

- **Controller Tests**: Test REST API endpoints using MockMvc
  - `CustomerControllerTest.java`
  - `SellerControllerTest.java`
  - `OwnerControllerTest.java`

- **Repository Tests**: Test data access layer using @DataJpaTest
  - `CustomerRepositoryTest.java`
  - `ProductStockRepositoryTest.java`
  - `CartItemRepositoryTest.java`

- **Mapper Tests**: Test DTO to Entity mapping
  - `CustomerMapperTest.java`
  - `ProductMapperTest.java`

### Integration Tests
- **Full End-to-End Tests**: Test complete request/response cycles
  - `CustomerIntegrationTest.java`

### Security Tests
- **Authentication & Authorization**: Test security configuration
  - `SecurityConfigTest.java`

### Performance Tests
- **Response Time Tests**: Ensure operations complete within acceptable time limits
  - `PerformanceTest.java`

### Test Utilities
- **Test Data Factory**: Provides test data objects
  - `TestDataFactory.java`
- **Test Configuration**: H2 database and mock configuration
  - `TestConfig.java`

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Suite
```bash
mvn test -Dtest=StoreManagerTestSuite
```

### Run Tests by Category
```bash
# Run only unit tests
mvn test -Dtest="**/*Test.java"

# Run only integration tests
mvn test -Dtest="**/*IntegrationTest.java"

# Run only repository tests
mvn test -Dtest="**/*RepositoryTest.java"
```

### Run Tests with Coverage Report
```bash
mvn clean test jacoco:report
```

The coverage report will be available at `target/site/jacoco/index.html`

## Test Configuration

### Test Database
Tests use an in-memory H2 database configured in `application-test.properties`:
- Database is created fresh for each test run
- Data is automatically cleaned up after tests
- SQL statements are logged for debugging

### Test Profiles
- Tests run with the `test` profile active
- Security is configured for testing with mock users
- Logging is set to DEBUG level for troubleshooting

## Test Dependencies

The following dependencies are used for testing:
- **JUnit 5**: Main testing framework
- **Mockito**: Mocking framework for unit tests
- **Spring Boot Test**: Integration testing support
- **Spring Security Test**: Security testing utilities
- **H2 Database**: In-memory database for tests
- **Testcontainers**: For more complex integration tests (if needed)

## Writing New Tests

### Service Tests
```java
@ExtendWith(MockitoExtension.class)
class YourServiceTest {
    @Mock
    private YourRepository repository;
    
    @InjectMocks
    private YourService service;
    
    @Test
    void testMethod_ShouldDoSomething() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        
        // Act
        ResultDto result = service.someMethod(1L);
        
        // Assert
        assertNotNull(result);
        verify(repository).findById(1L);
    }
}
```

### Controller Tests
```java
@WebMvcTest(YourController.class)
class YourControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private YourService service;
    
    @Test
    @WithMockUser
    void testEndpoint_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/endpoint"))
               .andExpect(status().isOk());
    }
}
```

### Repository Tests
```java
@DataJpaTest
class YourRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private YourRepository repository;
    
    @Test
    void testCustomQuery_ShouldReturnResults() {
        // Test repository methods
    }
}
```

## Test Coverage Goals

The project aims for:
- **Line Coverage**: > 80%
- **Branch Coverage**: > 70%
- **Method Coverage**: > 90%

## Continuous Integration

Tests are automatically run in CI/CD pipeline:
- On every pull request
- On merge to main branch
- Nightly builds for comprehensive testing

## Troubleshooting

### Common Issues

1. **Database Connection Issues**
   - Ensure H2 is in the classpath
   - Check test profile configuration

2. **Security Test Failures**
   - Verify @WithMockUser annotations
   - Check CSRF token inclusion in POST requests

3. **Performance Test Failures**
   - Tests may fail on slower machines
   - Adjust timeout values if necessary

### Debugging Tests
- Enable debug logging in `application-test.properties`
- Use H2 console for database inspection
- Add breakpoints in test methods

## Best Practices

1. **Test Naming**: Use descriptive test method names
2. **Test Structure**: Follow Arrange-Act-Assert pattern
3. **Test Data**: Use TestDataFactory for consistent test data
4. **Mocking**: Mock external dependencies, test real business logic
5. **Assertions**: Use specific assertions, avoid generic assertTrue
6. **Cleanup**: Ensure tests don't affect each other
7. **Documentation**: Comment complex test scenarios
