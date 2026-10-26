@echo off
call "%~dp0..\herramientas\env.bat"
cd /d "%~dp0"
echo.
echo === EJECUTANDO PRUEBAS (puede tardar un poco la primera vez) ===
echo.
call mvn -o -B -Dmaven.repo.local="%REPO%" test > resultado_pruebas.txt 2>&1
findstr /C:"Tests run:" /C:"BUILD" /C:"ERROR" /C:"FAIL" resultado_pruebas.txt
echo.
echo Detalle completo en resultado_pruebas.txt
pause
