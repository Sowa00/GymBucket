@echo off
echo Starting GymBucket Backend...
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
