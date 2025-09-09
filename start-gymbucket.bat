@echo off
echo ========================================
echo    GymBucket Application Launcher
echo ========================================
echo.

REM Set Java environment variables for backend
set JAVA_HOME=C:\Program Files\Zulu\zulu-24
set PATH=%JAVA_HOME%\bin;%PATH%

echo Starting GymBucket Backend and Frontend...
echo.

REM Start backend in a new window
echo [1/2] Starting Backend (Spring Boot)...
start "GymBucket Backend" cmd /k "cd /d \"%~dp0backend\" && \"%~dp0backend\mvnw.cmd\" spring-boot:run"

REM Wait a moment for backend to start
echo Waiting for backend to initialize...
timeout /t 10 /nobreak >nul

REM Start frontend in a new window
echo [2/2] Starting Frontend (Angular)...
start "GymBucket Frontend" cmd /k "cd /d \"%~dp0frontend\" && ng serve"

echo.
echo ========================================
echo    GymBucket is starting up!
echo ========================================
echo.
echo Backend:  http://localhost:8080
echo Frontend: http://localhost:4200
echo.
echo Test credentials:
echo Email:    a@a.com
echo Password: 2137
echo.
echo Press any key to close this window...
pause >nul
