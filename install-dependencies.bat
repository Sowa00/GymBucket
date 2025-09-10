@echo off
echo 📦 Installing GymBucket Dependencies for Windows...

echo.
echo This script will help you install the required dependencies.
echo Please make sure you have administrator privileges.
echo.

REM Check if Chocolatey is installed
choco --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Installing Chocolatey package manager...
    powershell -Command "Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))"
    echo Please restart your command prompt and run this script again.
    pause
    exit /b
)

echo Installing dependencies with Chocolatey...

REM Install Java 17
echo Installing Java 17...
choco install openjdk17 -y

REM Install Node.js 18
echo Installing Node.js 18...
choco install nodejs --version=18.19.0 -y

REM Install Maven
echo Installing Maven...
choco install maven -y

REM Install Docker Desktop
echo Installing Docker Desktop...
choco install docker-desktop -y

echo.
echo ✅ Dependencies installation completed!
echo.
echo Please restart your computer to ensure all environment variables are set.
echo After restart, you can run:
echo   - start-docker.bat (recommended)
echo   - start-local.bat (for local development)
echo.
pause
