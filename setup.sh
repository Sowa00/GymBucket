#!/bin/bash

# GymBucket Application Setup Script
# This script sets up the development environment for the GymBucket application

set -e  # Exit on any error

echo "🏋️‍♂️ Setting up GymBucket Application..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if running on Windows
if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" || "$OSTYPE" == "win32" ]]; then
    IS_WINDOWS=true
    print_warning "Detected Windows environment. Some commands may need adjustment."
else
    IS_WINDOWS=false
fi

# Check prerequisites
print_status "Checking prerequisites..."

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    print_error "Docker is not installed. Please install Docker Desktop from https://www.docker.com/products/docker-desktop"
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    print_error "Docker Compose is not installed. Please install Docker Compose."
    exit 1
fi

# Check if Java is installed (for local development)
if ! command -v java &> /dev/null; then
    print_warning "Java is not installed. You'll need Java 17+ for local development."
    print_warning "Download from: https://adoptium.net/"
fi

# Check if Node.js is installed (for local development)
if ! command -v node &> /dev/null; then
    print_warning "Node.js is not installed. You'll need Node.js 18+ for local development."
    print_warning "Download from: https://nodejs.org/"
fi

# Check if Maven is installed (for local development)
if ! command -v mvn &> /dev/null; then
    print_warning "Maven is not installed. You'll need Maven for local development."
    print_warning "Download from: https://maven.apache.org/download.cgi"
fi

print_success "Prerequisites check completed!"

# Create environment file if it doesn't exist
if [ ! -f .env ]; then
    print_status "Creating .env file..."
    cat > .env << EOF
# Database Configuration
MYSQL_ROOT_PASSWORD=rootpassword
MYSQL_DATABASE=gymbucket_dev
MYSQL_USER=gymuser
MYSQL_PASSWORD=gympassword

# Backend Configuration
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/gymbucket_dev?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=gymuser
SPRING_DATASOURCE_PASSWORD=gympassword
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false

# Frontend Configuration
API_URL=http://localhost:8080
EOF
    print_success ".env file created!"
fi

# Create start scripts
print_status "Creating start scripts..."

# Create start-docker.sh
cat > start-docker.sh << 'EOF'
#!/bin/bash
echo "🐳 Starting GymBucket with Docker Compose..."
docker-compose up --build
EOF

# Create start-local.sh
cat > start-local.sh << 'EOF'
#!/bin/bash
echo "🏋️‍♂️ Starting GymBucket locally..."

# Start MySQL with Docker
echo "Starting MySQL database..."
docker run -d \
  --name gymbucket-mysql \
  -e MYSQL_ROOT_PASSWORD=rootpassword \
  -e MYSQL_DATABASE=gymbucket_dev \
  -e MYSQL_USER=gymuser \
  -e MYSQL_PASSWORD=gympassword \
  -p 3306:3306 \
  mysql:8.0

# Wait for MySQL to be ready
echo "Waiting for MySQL to be ready..."
sleep 30

# Start backend
echo "Starting backend..."
cd backend
mvn spring-boot:run &
BACKEND_PID=$!

# Wait for backend to start
sleep 30

# Start frontend
echo "Starting frontend..."
cd ../frontend
npm install
npm start &
FRONTEND_PID=$!

echo "✅ Application started!"
echo "Backend: http://localhost:8080"
echo "Frontend: http://localhost:4200"
echo "Press Ctrl+C to stop all services"

# Wait for user to stop
wait
EOF

# Create stop-docker.sh
cat > stop-docker.sh << 'EOF'
#!/bin/bash
echo "🛑 Stopping GymBucket Docker containers..."
docker-compose down
EOF

# Create stop-local.sh
cat > stop-local.sh << 'EOF'
#!/bin/bash
echo "🛑 Stopping GymBucket local services..."

# Stop MySQL container
docker stop gymbucket-mysql 2>/dev/null || true
docker rm gymbucket-mysql 2>/dev/null || true

# Kill backend and frontend processes
pkill -f "spring-boot:run" 2>/dev/null || true
pkill -f "npm start" 2>/dev/null || true

echo "✅ All services stopped!"
EOF

# Make scripts executable
chmod +x start-docker.sh start-local.sh stop-docker.sh stop-local.sh

print_success "Start scripts created!"

# Create development setup script
print_status "Creating development setup script..."

cat > setup-dev.sh << 'EOF'
#!/bin/bash
echo "🛠️ Setting up development environment..."

# Install frontend dependencies
echo "Installing frontend dependencies..."
cd frontend
npm install
cd ..

# Build backend
echo "Building backend..."
cd backend
mvn clean install -DskipTests
cd ..

echo "✅ Development environment ready!"
echo "Use ./start-local.sh to start the application locally"
echo "Use ./start-docker.sh to start with Docker"
EOF

chmod +x setup-dev.sh

print_success "Development setup script created!"

# Create README
print_status "Creating comprehensive README..."

cat > README-SETUP.md << 'EOF'
# GymBucket Application Setup Guide

## 🏋️‍♂️ Quick Start with Docker (Recommended)

### Prerequisites
- Docker Desktop
- Docker Compose

### 1. Clone the Repository
```bash
git clone <repository-url>
cd GymBucket
```

### 2. Run Setup Script
```bash
chmod +x setup.sh
./setup.sh
```

### 3. Start the Application
```bash
./start-docker.sh
```

The application will be available at:
- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8080
- **Database**: localhost:3306

### 4. Stop the Application
```bash
./stop-docker.sh
```

## 🛠️ Local Development Setup

### Prerequisites
- Java 17+
- Node.js 18+
- Maven 3.6+
- MySQL 8.0+ (or Docker for MySQL)

### 1. Setup Development Environment
```bash
./setup-dev.sh
```

### 2. Start Locally
```bash
./start-local.sh
```

### 3. Stop Locally
```bash
./stop-local.sh
```

## 📁 Project Structure

```
GymBucket/
├── backend/                 # Spring Boot Backend
│   ├── src/
│   └── pom.xml
├── frontend/               # Angular Frontend
│   ├── src/
│   └── package.json
├── docker-compose.yml      # Docker Compose configuration
├── Dockerfile.backend      # Backend Docker image
├── Dockerfile.frontend     # Frontend Docker image
├── setup.sh               # Setup script
├── start-docker.sh        # Start with Docker
├── start-local.sh         # Start locally
├── stop-docker.sh         # Stop Docker containers
└── stop-local.sh          # Stop local services
```

## 🔧 Configuration

### Environment Variables
The application uses the following environment variables (configured in `.env`):

```env
# Database
MYSQL_ROOT_PASSWORD=rootpassword
MYSQL_DATABASE=gymbucket_dev
MYSQL_USER=gymuser
MYSQL_PASSWORD=gympassword

# Backend
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/gymbucket_dev
SPRING_DATASOURCE_USERNAME=gymuser
SPRING_DATASOURCE_PASSWORD=gympassword
```

### Database
- **Host**: localhost
- **Port**: 3306
- **Database**: gymbucket_dev
- **Username**: gymuser
- **Password**: gympassword

## 🚀 Deployment

### Production Deployment
1. Update environment variables in `.env`
2. Build and deploy with Docker:
```bash
docker-compose -f docker-compose.yml up -d --build
```

### Health Checks
- Backend health: http://localhost:8080/actuator/health
- Frontend: http://localhost:4200

## 🐛 Troubleshooting

### Common Issues

1. **Port already in use**
   - Stop existing services: `./stop-docker.sh` or `./stop-local.sh`
   - Check for running processes: `docker ps` or `netstat -tulpn`

2. **Database connection issues**
   - Ensure MySQL is running: `docker ps | grep mysql`
   - Check database credentials in `.env`

3. **Frontend build issues**
   - Clear node_modules: `rm -rf frontend/node_modules && npm install`
   - Check Node.js version: `node --version` (should be 18+)

4. **Backend build issues**
   - Clear Maven cache: `mvn clean`
   - Check Java version: `java --version` (should be 17+)

### Logs
- Docker logs: `docker-compose logs -f [service-name]`
- Backend logs: Check console output or `backend/logs/`
- Frontend logs: Check browser console

## 📞 Support

For issues and questions:
1. Check the troubleshooting section
2. Review application logs
3. Create an issue in the repository

## 🔐 Default Credentials

The application comes with default test credentials:
- **Email**: a@a.com
- **Password**: 2137

**Note**: Change these credentials in production!
EOF

print_success "README created!"

# Final instructions
print_success "🎉 Setup completed successfully!"
echo ""
echo "📋 Next steps:"
echo "1. Run './start-docker.sh' to start with Docker (recommended)"
echo "2. Or run './setup-dev.sh' then './start-local.sh' for local development"
echo "3. Access the application at http://localhost:4200"
echo ""
echo "📚 For detailed instructions, see README-SETUP.md"
echo ""
echo "🔐 Default login credentials:"
echo "   Email: a@a.com"
echo "   Password: 2137"
