#!/bin/bash

# Deployment script for production environment

set -e  # Exit on any error

echo "🚀 Starting deployment process..."

# Configuration
COMPOSE_FILE="compose.prod.yml"
BACKUP_DIR="./backups"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")

# Create backup directory if it doesn't exist
mkdir -p $BACKUP_DIR

# Function to check if service is healthy
check_health() {
    local service=$1
    local max_attempts=30
    local attempt=1
    
    echo "⏳ Waiting for $service to be healthy..."
    
    while [ $attempt -le $max_attempts ]; do
        if docker-compose -f $COMPOSE_FILE ps $service | grep -q "healthy"; then
            echo "✅ $service is healthy"
            return 0
        fi
        
        echo "Attempt $attempt/$max_attempts: $service not healthy yet..."
        sleep 10
        ((attempt++))
    done
    
    echo "❌ $service failed to become healthy"
    return 1
}

# Backup database
echo "📦 Creating database backup..."
docker-compose -f $COMPOSE_FILE exec database pg_dump -U $POSTGRES_USER -d $POSTGRES_DB > "$BACKUP_DIR/db_backup_$TIMESTAMP.sql"

# Pull latest images
echo "📥 Pulling latest images..."
docker-compose -f $COMPOSE_FILE pull

# Stop services gracefully
echo "🛑 Stopping services..."
docker-compose -f $COMPOSE_FILE down --remove-orphans

# Start services
echo "🚀 Starting services..."
docker-compose -f $COMPOSE_FILE up -d

# Wait for services to be healthy
check_health "database"
check_health "backend"
check_health "frontend"

# Run any necessary migrations
echo "🔄 Running database migrations..."
docker-compose -f $COMPOSE_FILE exec backend java -jar app.jar --spring.profiles.active=migration || true

# Cleanup old backups (keep last 7 days)
echo "🧹 Cleaning up old backups..."
find $BACKUP_DIR -name "db_backup_*.sql" -mtime +7 -delete

echo "✅ Deployment completed successfully!"
echo "🌐 Application is available at:"
echo "   Frontend: http://localhost"
echo "   Backend API: http://localhost:8080"
echo "   Monitoring: http://localhost:3001 (Grafana)"
