@echo off
echo 🏋️‍♂️ Starting GymBucket locally...

REM Start MySQL with Docker
echo Starting MySQL database...
docker run -d --name gymbucket-mysql -e MYSQL_ROOT_PASSWORD=rootpassword -e MYSQL_DATABASE=gymbucket_dev -e MYSQL_USER=gymuser -e MYSQL_PASSWORD=gympassword -p 3306:3306 mysql:8.0

REM Wait for MySQL to be ready
echo Waiting for MySQL to be ready...
timeout /t 30 /nobreak > nul

REM Start backend
echo Starting backend...
cd backend
start "Backend" cmd /k "mvn spring-boot:run"
cd ..

REM Wait for backend to start
timeout /t 30 /nobreak > nul

REM Start frontend
echo Starting frontend...
cd frontend
start "Frontend" cmd /k "npm start"
cd ..

echo ✅ Application started!
echo Backend: http://localhost:8080
echo Frontend: http://localhost:4200
echo Press any key to stop all services
pause

REM Stop services
echo Stopping services...
docker stop gymbucket-mysql
docker rm gymbucket-mysql
taskkill /f /im java.exe 2>nul
taskkill /f /im node.exe 2>nul
echo ✅ All services stopped!
pause
