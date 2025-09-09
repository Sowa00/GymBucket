@echo off
echo Starting GymBucket Frontend...
echo.

REM Navigate to frontend directory
cd /d "%~dp0frontend"

REM Start the Angular development server
echo Starting Angular frontend on port 4200...
ng serve

pause
