# 🚀 GymBucket Deployment Guide

## Quick Start (Recommended)

### Option 1: Docker (Easiest)
```bash
# 1. Clone the repository
git clone <repository-url>
cd GymBucket

# 2. Run setup (Linux/Mac)
chmod +x setup.sh
./setup.sh

# 3. Start the application
./start-docker.sh
```

### Option 2: Windows
```cmd
# 1. Clone the repository
git clone <repository-url>
cd GymBucket

# 2. Install dependencies
install-dependencies.bat

# 3. Start the application
start-docker.bat
```

## 📋 Prerequisites

### Required Software
- **Docker Desktop** (recommended)
- **Docker Compose**

### For Local Development
- **Java 17+**
- **Node.js 18+**
- **Maven 3.6+**
- **MySQL 8.0+** (or use Docker)

## 🐳 Docker Deployment

### 1. Using Docker Compose (Recommended)
```bash
# Start all services
docker-compose up --build

# Start in background
docker-compose up -d --build

# Stop services
docker-compose down
```

### 2. Individual Docker Images
```bash
# Build backend
docker build -f Dockerfile.backend -t gymbucket-backend .

# Build frontend
docker build -f Dockerfile.frontend -t gymbucket-frontend .

# Run with custom configuration
docker run -d --name gymbucket-mysql \
  -e MYSQL_ROOT_PASSWORD=rootpassword \
  -e MYSQL_DATABASE=gymbucket_dev \
  -p 3306:3306 \
  mysql:8.0

docker run -d --name gymbucket-backend \
  -p 8080:8080 \
  --link gymbucket-mysql:mysql \
  gymbucket-backend

docker run -d --name gymbucket-frontend \
  -p 4200:4200 \
  gymbucket-frontend
```

## 🛠️ Local Development Setup

### 1. Install Dependencies

#### Linux/Mac
```bash
chmod +x install-dependencies.sh
./install-dependencies.sh
```

#### Windows
```cmd
install-dependencies.bat
```

### 2. Start Services

#### Linux/Mac
```bash
# Start with Docker MySQL
./start-local.sh

# Or start everything locally
./setup-dev.sh
```

#### Windows
```cmd
start-local.bat
```

## 🔧 Configuration

### Environment Variables
Create a `.env` file in the project root:

```env
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
```

### Database Configuration
- **Host**: localhost
- **Port**: 3306
- **Database**: gymbucket_dev
- **Username**: gymuser
- **Password**: gympassword

## 🌐 Access Points

After successful deployment:
- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8080
- **Database**: localhost:3306
- **Health Check**: http://localhost:8080/actuator/health

## 🔐 Default Credentials

```
Email: a@a.com
Password: 2137
```

**⚠️ Important**: Change these credentials in production!

## 📊 Production Deployment

### 1. Environment Setup
```bash
# Update environment variables for production
export MYSQL_ROOT_PASSWORD=your_secure_password
export MYSQL_PASSWORD=your_secure_password
export SPRING_PROFILES_ACTIVE=production
```

### 2. Docker Compose Production
```yaml
# docker-compose.prod.yml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      MYSQL_DATABASE: gymbucket_prod
    volumes:
      - mysql_prod_data:/var/lib/mysql
    restart: unless-stopped

  backend:
    build: .
    environment:
      SPRING_PROFILES_ACTIVE: production
      SPRING_DATASOURCE_PASSWORD: ${MYSQL_PASSWORD}
    depends_on:
      - mysql
    restart: unless-stopped

  frontend:
    build: .
    restart: unless-stopped
```

### 3. Deploy to Production
```bash
docker-compose -f docker-compose.prod.yml up -d --build
```

## 🔍 Monitoring & Logs

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mysql
```

### Health Checks
```bash
# Backend health
curl http://localhost:8080/actuator/health

# Database connection
docker exec -it gymbucket-mysql mysql -u gymuser -p gymbucket_dev
```

## 🐛 Troubleshooting

### Common Issues

1. **Port Already in Use**
   ```bash
   # Check what's using the port
   netstat -tulpn | grep :8080
   netstat -tulpn | grep :4200
   
   # Kill process
   sudo kill -9 <PID>
   ```

2. **Database Connection Issues**
   ```bash
   # Check MySQL container
   docker ps | grep mysql
   
   # Check MySQL logs
   docker logs gymbucket-mysql
   
   # Test connection
   docker exec -it gymbucket-mysql mysql -u gymuser -p
   ```

3. **Frontend Build Issues**
   ```bash
   # Clear node modules
   rm -rf frontend/node_modules
   cd frontend && npm install
   
   # Check Node version
   node --version  # Should be 18+
   ```

4. **Backend Build Issues**
   ```bash
   # Clear Maven cache
   cd backend && mvn clean
   
   # Check Java version
   java --version  # Should be 17+
   ```

### Performance Optimization

1. **Database Optimization**
   ```sql
   -- Add indexes for better performance
   CREATE INDEX idx_client_email ON clients(email);
   CREATE INDEX idx_workout_plan_created_by ON workout_plans(created_by_id);
   ```

2. **Docker Optimization**
   ```bash
   # Use multi-stage builds
   # Optimize image layers
   # Use .dockerignore
   ```

## 📈 Scaling

### Horizontal Scaling
```yaml
# docker-compose.scale.yml
version: '3.8'
services:
  backend:
    deploy:
      replicas: 3
    ports:
      - "8080-8082:8080"
  
  frontend:
    deploy:
      replicas: 2
    ports:
      - "4200-4201:4200"
```

### Load Balancer
```yaml
# Add nginx load balancer
nginx:
  image: nginx:alpine
  ports:
    - "80:80"
  volumes:
    - ./nginx.conf:/etc/nginx/nginx.conf
```

## 🔒 Security Considerations

1. **Change Default Passwords**
2. **Use HTTPS in Production**
3. **Configure Firewall Rules**
4. **Regular Security Updates**
5. **Database Encryption**
6. **API Rate Limiting**

## 📞 Support

For deployment issues:
1. Check the troubleshooting section
2. Review application logs
3. Create an issue in the repository

## 📚 Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Spring Boot Deployment](https://spring.io/guides/gs/spring-boot-docker/)
- [Angular Deployment](https://angular.io/guide/deployment)
- [MySQL Configuration](https://dev.mysql.com/doc/refman/8.0/en/)
