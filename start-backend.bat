@echo off
echo Starting GymBucket Backend...
echo.

REM Kill any process using port 8080
echo Checking for processes on port 8080...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080') do (
    echo Killing process %%a on port 8080...
    taskkill /f /pid %%a >nul 2>&1
)
echo Port 8080 is now free.
echo.

REM Set Java environment variables
set JAVA_HOME=C:\Program Files\Zulu\zulu-24
set PATH=%JAVA_HOME%\bin;%PATH%

REM Navigate to backend directory
cd /d "%~dp0backend"

REM Start the backend using Maven wrapper
echo Starting Spring Boot backend on port 8080...
"%~dp0backend\mvnw.cmd" spring-boot:run

pause
