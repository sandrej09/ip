@echo off
setlocal
cd ..

if not exist data mkdir data
type nul > data\lumi.txt

if exist out rmdir /s /q out
mkdir out
javac -encoding UTF-8 -d out src\main\java\lumi\*.java
if errorlevel 1 (
  echo Compile FAILED
  exit /b 1
)

java -cp out lumi.Lumi < tui\input.txt > tui\ACTUAL.TXT
if errorlevel 1 (
  echo Run FAILED
  exit /b 1
)

fc /n tui\EXPECTED.TXT tui\ACTUAL.TXT >nul
if errorlevel 1 (
  echo FAIL
  exit /b 1
) else (
  echo PASS
  exit /b 0
)
