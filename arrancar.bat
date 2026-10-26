@echo off
call "%~dp0..\herramientas\env.bat"
cd /d "%~dp0"
echo.
echo === ARRANCANDO CLINICA. Abre http://localhost:8080 en el navegador. Ctrl+C para parar ===
echo.
call mvn -o -B -Dmaven.repo.local="%REPO%" spring-boot:run
pause
