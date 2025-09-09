# GymBucket Application Launcher (PowerShell)
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "    GymBucket Application Launcher" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Set Java environment variables
$env:JAVA_HOME = "C:\Program Files\Zulu\zulu-24"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

Write-Host "Starting GymBucket Backend and Frontend..." -ForegroundColor Green
Write-Host ""

# Get the script directory
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path

# Start backend in a new window
Write-Host "[1/2] Starting Backend (Spring Boot)..." -ForegroundColor Yellow
$backendPath = Join-Path $scriptDir "backend"
$mvnwPath = Join-Path $backendPath "mvnw.cmd"
Start-Process -FilePath "cmd" -ArgumentList "/k", "cd /d `"$backendPath`" && `"$mvnwPath`" spring-boot:run" -WindowStyle Normal

# Wait for backend to start
Write-Host "Waiting for backend to initialize..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# Start frontend in a new window
Write-Host "[2/2] Starting Frontend (Angular)..." -ForegroundColor Yellow
$frontendPath = Join-Path $scriptDir "frontend"
Start-Process -FilePath "cmd" -ArgumentList "/k", "cd /d `"$frontendPath`" && ng serve" -WindowStyle Normal

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "    GymBucket is starting up!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Backend:  http://localhost:8080" -ForegroundColor Green
Write-Host "Frontend: http://localhost:4200" -ForegroundColor Green
Write-Host ""
Write-Host "Test credentials:" -ForegroundColor Yellow
Write-Host "Email:    a@a.com" -ForegroundColor White
Write-Host "Password: 2137" -ForegroundColor White
Write-Host ""
Write-Host "Press any key to close this window..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
