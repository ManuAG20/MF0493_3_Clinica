@echo off
call "%~dp0..\herramientas\env.bat"
cd /d "%~dp0"
echo.
echo === EMPAQUETANDO ===
echo.
call mvn -o -B -Dmaven.repo.local="%REPO%" package -DskipTests
echo.
if exist target\app.jar (echo OK: se ha creado target\app.jar) else (echo ERROR: no se ha creado el jar)
pause
