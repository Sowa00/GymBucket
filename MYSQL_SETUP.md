# MySQL Database Setup for GymBucket

## Prerequisites

Before running the application with MySQL, you need to install and configure MySQL on your system.

## Installation

### Windows

1. **Download MySQL Installer**
   - Go to: https://dev.mysql.com/downloads/installer/
   - Download "MySQL Installer for Windows"

2. **Install MySQL**
   - Run the installer
   - Choose "Developer Default" installation
   - Set root password (remember this password!)
   - Complete the installation

3. **Verify Installation**
   - Open Command Prompt
   - Run: `mysql --version`
   - You should see MySQL version information

### macOS

1. **Install using Homebrew**
   ```bash
   brew install mysql
   ```

2. **Start MySQL service**
   ```bash
   brew services start mysql
   ```

3. **Secure installation**
   ```bash
   mysql_secure_installation
   ```

### Linux (Ubuntu/Debian)

1. **Update package list**
   ```bash
   sudo apt update
   ```

2. **Install MySQL**
   ```bash
   sudo apt install mysql-server
   ```

3. **Secure installation**
   ```bash
   sudo mysql_secure_installation
   ```

## Database Configuration

### 1. Create Database

Connect to MySQL and create the database:

```sql
mysql -u root -p
```

```sql
CREATE DATABASE gymbucket_dev;
CREATE DATABASE gymbucket;
```

### 2. Create User (Optional but Recommended)

```sql
CREATE USER 'gymbucket'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON gymbucket_dev.* TO 'gymbucket'@'localhost';
GRANT ALL PRIVILEGES ON gymbucket.* TO 'gymbucket'@'localhost';
FLUSH PRIVILEGES;
```

## Environment Variables

Set these environment variables before starting the application:

### Windows (Command Prompt)
```cmd
set DB_USERNAME=root
set DB_PASSWORD=your_mysql_password
```

### Windows (PowerShell)
```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "your_mysql_password"
```

### macOS/Linux
```bash
export DB_USERNAME=root
export DB_PASSWORD=your_mysql_password
```

## Starting the Application

### Option 1: Using the MySQL startup script
```cmd
start-backend-mysql.bat
```

### Option 2: Using PowerShell
```powershell
.\start-backend-mysql.ps1
```

### Option 3: Manual start
```cmd
set SPRING_PROFILES_ACTIVE=mysql
cd backend
mvn spring-boot:run
```

## Verification

1. **Check database connection**
   - Look for "Started BackendApplication" in the logs
   - No database connection errors should appear

2. **Verify tables are created**
   ```sql
   mysql -u root -p
   USE gymbucket_dev;
   SHOW TABLES;
   ```

3. **Test the application**
   - Go to http://localhost:4200
   - Login with: a@a.com / 2137
   - Create some data and restart the backend
   - Data should persist!

## Troubleshooting

### Common Issues

1. **Connection refused**
   - Make sure MySQL service is running
   - Check if port 3306 is available

2. **Access denied**
   - Verify username and password
   - Check if user has proper privileges

3. **Database doesn't exist**
   - Create the database manually
   - Check database name in configuration

### Useful Commands

```sql
-- Check MySQL status
SHOW PROCESSLIST;

-- Check databases
SHOW DATABASES;

-- Check user privileges
SHOW GRANTS FOR 'root'@'localhost';
```

## Production Notes

For production deployment:

1. Use strong passwords
2. Create dedicated database user
3. Enable SSL connections
4. Configure proper firewall rules
5. Set up database backups
6. Use connection pooling
7. Monitor database performance

