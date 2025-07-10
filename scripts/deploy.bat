@echo off
REM Deployment script for Windows production environment

echo 🚀 Starting deployment process...

REM Configuration
set COMPOSE_FILE=compose.prod.yml
set BACKUP_DIR=.\backups
set TIMESTAMP=%date:~-4,4%%date:~-10,2%%date:~-7,2%_%time:~0,2%%time:~3,2%%time:~6,2%
set TIMESTAMP=%TIMESTAMP: =0%

REM Create backup directory if it doesn't exist
if not exist %BACKUP_DIR% mkdir %BACKUP_DIR%

REM Backup database
echo 📦 Creating database backup...
docker-compose -f %COMPOSE_FILE% exec database pg_dump -U %POSTGRES_USER% -d %POSTGRES_DB% > "%BACKUP_DIR%\db_backup_%TIMESTAMP%.sql"

REM Pull latest images
echo 📥 Pulling latest images...
docker-compose -f %COMPOSE_FILE% pull

REM Stop services gracefully
echo 🛑 Stopping services...
docker-compose -f %COMPOSE_FILE% down --remove-orphans

REM Start services
echo 🚀 Starting services...
docker-compose -f %COMPOSE_FILE% up -d

REM Wait for services
echo ⏳ Waiting for services to start...
timeout /t 60 /nobreak

echo ✅ Deployment completed!
echo 🌐 Application is available at:
echo    Frontend: http://localhost
echo    Backend API: http://localhost:8080
echo    Monitoring: http://localhost:3001 (Grafana)

pause
