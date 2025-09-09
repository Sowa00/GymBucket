# PowerShell script to start GymBucket Backend with MySQL
Write-Host "Starting GymBucket Backend with MySQL..." -ForegroundColor Green
Write-Host ""

# Set MySQL profile
$env:SPRING_PROFILES_ACTIVE = "mysql"

# Start the backend
Set-Location backend
mvn spring-boot:run

