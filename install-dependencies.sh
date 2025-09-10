#!/bin/bash

# GymBucket Dependencies Installation Script
# This script installs all required dependencies for the GymBucket application

set -e  # Exit on any error

echo "📦 Installing GymBucket Dependencies..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

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

# Detect OS
if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    OS="linux"
elif [[ "$OSTYPE" == "darwin"* ]]; then
    OS="macos"
elif [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" || "$OSTYPE" == "win32" ]]; then
    OS="windows"
else
    OS="unknown"
fi

print_status "Detected OS: $OS"

# Function to install Docker
install_docker() {
    print_status "Installing Docker..."
    
    case $OS in
        "linux")
            # Ubuntu/Debian
            if command -v apt-get &> /dev/null; then
                sudo apt-get update
                sudo apt-get install -y apt-transport-https ca-certificates curl gnupg lsb-release
                curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
                echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
                sudo apt-get update
                sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
            # CentOS/RHEL/Fedora
            elif command -v yum &> /dev/null; then
                sudo yum install -y yum-utils
                sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
                sudo yum install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
            fi
            sudo systemctl start docker
            sudo systemctl enable docker
            sudo usermod -aG docker $USER
            ;;
        "macos")
            print_warning "Please install Docker Desktop for Mac from: https://www.docker.com/products/docker-desktop"
            ;;
        "windows")
            print_warning "Please install Docker Desktop for Windows from: https://www.docker.com/products/docker-desktop"
            ;;
    esac
}

# Function to install Java
install_java() {
    print_status "Installing Java 17..."
    
    case $OS in
        "linux")
            if command -v apt-get &> /dev/null; then
                sudo apt-get update
                sudo apt-get install -y openjdk-17-jdk
            elif command -v yum &> /dev/null; then
                sudo yum install -y java-17-openjdk-devel
            fi
            ;;
        "macos")
            if command -v brew &> /dev/null; then
                brew install openjdk@17
            else
                print_warning "Please install Java 17 from: https://adoptium.net/"
            fi
            ;;
        "windows")
            print_warning "Please install Java 17 from: https://adoptium.net/"
            ;;
    esac
}

# Function to install Node.js
install_nodejs() {
    print_status "Installing Node.js 18..."
    
    case $OS in
        "linux")
            curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
            sudo apt-get install -y nodejs
            ;;
        "macos")
            if command -v brew &> /dev/null; then
                brew install node@18
            else
                print_warning "Please install Node.js 18 from: https://nodejs.org/"
            fi
            ;;
        "windows")
            print_warning "Please install Node.js 18 from: https://nodejs.org/"
            ;;
    esac
}

# Function to install Maven
install_maven() {
    print_status "Installing Maven..."
    
    case $OS in
        "linux")
            if command -v apt-get &> /dev/null; then
                sudo apt-get install -y maven
            elif command -v yum &> /dev/null; then
                sudo yum install -y maven
            fi
            ;;
        "macos")
            if command -v brew &> /dev/null; then
                brew install maven
            else
                print_warning "Please install Maven from: https://maven.apache.org/download.cgi"
            fi
            ;;
        "windows")
            print_warning "Please install Maven from: https://maven.apache.org/download.cgi"
            ;;
    esac
}

# Function to install MySQL (optional, since we use Docker)
install_mysql() {
    print_status "Installing MySQL (optional - Docker will be used by default)..."
    
    case $OS in
        "linux")
            if command -v apt-get &> /dev/null; then
                sudo apt-get install -y mysql-server
            elif command -v yum &> /dev/null; then
                sudo yum install -y mysql-server
            fi
            ;;
        "macos")
            if command -v brew &> /dev/null; then
                brew install mysql
            else
                print_warning "Please install MySQL from: https://dev.mysql.com/downloads/mysql/"
            fi
            ;;
        "windows")
            print_warning "Please install MySQL from: https://dev.mysql.com/downloads/mysql/"
            ;;
    esac
}

# Check what's already installed
print_status "Checking existing installations..."

# Check Docker
if command -v docker &> /dev/null; then
    print_success "Docker is already installed: $(docker --version)"
else
    print_warning "Docker not found. Installing..."
    install_docker
fi

# Check Docker Compose
if command -v docker-compose &> /dev/null; then
    print_success "Docker Compose is already installed: $(docker-compose --version)"
else
    print_warning "Docker Compose not found. Installing..."
    case $OS in
        "linux")
            sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
            sudo chmod +x /usr/local/bin/docker-compose
            ;;
        "macos"|"windows")
            print_warning "Docker Compose should be included with Docker Desktop"
            ;;
    esac
fi

# Check Java
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -ge 17 ]; then
        print_success "Java is already installed: $(java -version 2>&1 | head -n 1)"
    else
        print_warning "Java version $JAVA_VERSION found, but Java 17+ is required. Installing Java 17..."
        install_java
    fi
else
    print_warning "Java not found. Installing Java 17..."
    install_java
fi

# Check Node.js
if command -v node &> /dev/null; then
    NODE_VERSION=$(node --version | cut -d'v' -f2 | cut -d'.' -f1)
    if [ "$NODE_VERSION" -ge 18 ]; then
        print_success "Node.js is already installed: $(node --version)"
    else
        print_warning "Node.js version $NODE_VERSION found, but Node.js 18+ is required. Installing Node.js 18..."
        install_nodejs
    fi
else
    print_warning "Node.js not found. Installing Node.js 18..."
    install_nodejs
fi

# Check Maven
if command -v mvn &> /dev/null; then
    print_success "Maven is already installed: $(mvn --version | head -n 1)"
else
    print_warning "Maven not found. Installing Maven..."
    install_maven
fi

# Check MySQL (optional)
if command -v mysql &> /dev/null; then
    print_success "MySQL is already installed: $(mysql --version)"
else
    print_status "MySQL not found, but it's optional since Docker will be used by default."
    read -p "Do you want to install MySQL locally? (y/n): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        install_mysql
    fi
fi

# Install frontend dependencies
print_status "Installing frontend dependencies..."
if [ -d "frontend" ]; then
    cd frontend
    npm install
    cd ..
    print_success "Frontend dependencies installed!"
else
    print_warning "Frontend directory not found. Make sure you're in the project root."
fi

# Build backend
print_status "Building backend..."
if [ -d "backend" ]; then
    cd backend
    mvn clean install -DskipTests
    cd ..
    print_success "Backend built successfully!"
else
    print_warning "Backend directory not found. Make sure you're in the project root."
fi

print_success "🎉 All dependencies installed successfully!"
echo ""
echo "📋 Next steps:"
echo "1. Run './start-docker.sh' to start with Docker (recommended)"
echo "2. Or run './start-local.sh' for local development"
echo "3. Access the application at http://localhost:4200"
echo ""
echo "🔐 Default login credentials:"
echo "   Email: a@a.com"
echo "   Password: 2137"
