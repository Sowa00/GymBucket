@echo off
echo Stopping GymBucket Services...
echo.

REM Kill Java processes (Spring Boot backend)
echo Stopping Backend (Spring Boot)...
taskkill /f /im java.exe 2>nul
if %errorlevel% equ 0 (
    echo Backend stopped successfully.
) else (
    echo No backend process found.
)

REM Kill Node processes (Angular frontend)
echo Stopping Frontend (Angular)...
taskkill /f /im node.exe 2>nul
if %errorlevel% equ 0 (
    echo Frontend stopped successfully.
) else (
    echo No frontend process found.
)

echo.
echo All GymBucket services have been stopped.
pause
