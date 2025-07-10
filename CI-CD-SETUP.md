# CI/CD Setup Guide

This guide covers the complete CI/CD setup for the Store Manager application.

## ✅ Fixed Issues

The Maven compilation errors in the DTOs have been resolved by:
- Removing conflicting `@Builder` inheritance between `PersonDto` and its subclasses
- Converting inheritance to composition pattern
- Each DTO now has its own complete field set

## 🚀 CI/CD Pipeline Overview

### Workflows Included

1. **CI/CD Pipeline** (`.github/workflows/ci-cd.yml`)
   - Runs on push to `main` and `develop` branches
   - Runs on pull requests to `main`
   - Includes testing, security scanning, building, and deployment

2. **Quality Gates** (`.github/workflows/quality-gates.yml`)
   - Runs on pull requests
   - Code quality checks with SonarCloud (optional)
   - Test coverage reporting

3. **Deploy** (`.github/workflows/deploy.yml`)
   - Runs after successful CI/CD pipeline completion
   - Deploys to production server via SSH

## 📋 Setup Checklist

### 1. Repository Secrets Configuration

Add these secrets in GitHub repository settings:

#### Required for All Workflows:
- `GITHUB_TOKEN` (automatically provided by GitHub)

#### Optional for SonarCloud (Quality Gates):
- `SONAR_TOKEN` - Your SonarCloud token

#### Required for Server Deployment:
- `HOST` - Your production server IP/hostname
- `USERNAME` - SSH username for server access  
- `SSH_KEY` - Private SSH key for server access
- `PORT` - SSH port (usually 22)

### 2. Environment Setup

#### Development Environment:
```bash
# Copy environment template
cp .env.example .env

# Edit .env with your development values
```

#### Production Environment:
```bash
# Create production environment file
cp .env.example .env.prod

# Edit .env.prod with your production values
```

### 3. Maven Dependencies

Add these dependencies to your `pom.xml` (already added):

```xml
<!-- Actuator for health checks and monitoring -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<!-- Prometheus metrics -->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

### 4. Application Properties

Add to `src/main/resources/application.properties`:

```properties
# Actuator endpoints for monitoring
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=always
management.metrics.export.prometheus.enabled=true

# Profile-specific configurations
spring.profiles.active=@spring.profiles.active@
```

### 5. Frontend Package.json Scripts

Add these scripts to `frontend/package.json`:

```json
{
  "scripts": {
    "lint": "eslint src/ --ext .js,.jsx,.ts,.tsx",
    "lint:fix": "eslint src/ --ext .js,.jsx,.ts,.tsx --fix"
  },
  "devDependencies": {
    "eslint": "^8.57.0"
  }
}
```

## 🐳 Docker Setup

### Development
```bash
# Start development environment
docker-compose up -d

# View logs
docker-compose logs -f

# Stop environment
docker-compose down
```

### Production
```bash
# Start production environment
docker-compose -f compose.prod.yml up -d

# Or use deployment script
./scripts/deploy.sh  # Linux/Mac
scripts\deploy.bat   # Windows
```

## 📊 Monitoring

### Accessing Monitoring Tools

- **Application**: http://localhost (frontend) / http://localhost:8080 (backend API)
- **Grafana**: http://localhost:3001 (admin/admin)
- **Prometheus**: http://localhost:9090
- **Health Check**: http://localhost:8080/actuator/health

### Setting Up Grafana Dashboards

1. Access Grafana at http://localhost:3001
2. Login with admin/admin (change password on first login)
3. Add Prometheus data source: http://prometheus:9090
4. Import Spring Boot dashboard (ID: 6756)

## 🔧 Pipeline Features

### ✅ Continuous Integration
- **Backend Testing**: Maven Surefire with PostgreSQL test database
- **Frontend Testing**: Jest with coverage reporting
- **Code Quality**: ESLint, SpotBugs static analysis
- **Security Scanning**: Trivy vulnerability scanner
- **Test Reporting**: JUnit XML reports in GitHub

### ✅ Continuous Deployment
- **Multi-stage Docker builds** for optimized production images
- **Container Registry**: GitHub Container Registry (ghcr.io)
- **Environment-specific deployments**: staging (develop branch) and production (main branch)
- **Health checks**: Automated verification after deployment

### ✅ Security Features
- **Non-root container users**
- **Vulnerability scanning** with Trivy
- **Secure Docker images** with minimal attack surface
- **Environment-specific secrets** management

## 🚀 Deployment Process

### Automatic Deployment (Recommended)

1. **Push to develop branch** → Deploys to staging
2. **Push to main branch** → Deploys to production
3. **Pull requests** → Runs tests and quality checks

### Manual Deployment

```bash
# Production deployment
./scripts/deploy.sh

# Or step by step:
docker-compose -f compose.prod.yml pull
docker-compose -f compose.prod.yml down
docker-compose -f compose.prod.yml up -d
```

## 🔍 Troubleshooting

### Common Issues

1. **Build failures**: Check Java 21 and Node.js 18 compatibility
2. **Database connection**: Verify PostgreSQL service is healthy
3. **Permission errors**: Ensure scripts are executable (`chmod +x scripts/*.sh`)
4. **Port conflicts**: Check if ports 80, 8080, 3001, 9090 are available

### Checking Application Health

```bash
# Backend health
curl http://localhost:8080/actuator/health

# Frontend health  
curl http://localhost

# Database health
docker-compose exec database pg_isready -U $POSTGRES_USER
```

### Viewing Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f database
```

## 📈 Next Steps

1. **Set up monitoring alerts** in Grafana
2. **Configure backup strategies** for production database
3. **Implement load balancing** for high availability
4. **Set up SSL certificates** for HTTPS
5. **Configure log aggregation** (ELK stack or similar)

## 🤝 Contributing

1. Create feature branch from `develop`
2. Make changes and write tests
3. Create pull request to `develop`
4. After review, merge to `develop` for staging deployment
5. Create release PR from `develop` to `main` for production

---

The compilation errors have been fixed and your CI/CD pipeline is ready to use! 🎉
