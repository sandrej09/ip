@echo off
setlocal enabledelayedexpansion

pushd ..

if exist out rmdir /s /q out
mkdir out


if exist data del /q data\*.txt >nul 2>&1

del /q sources.txt >nul 2>&1
for /r src %%f in (*.java) do echo %%f>> sources.txt
javac -encoding UTF-8 -d out @sources.txt
if errorlevel 1 (
  echo Compile FAILED
  popd & exit /b 1
)

java -Duser.language=en -Duser.country=US -cp out lumi.Lumi ^
  < text-ui-test\input.txt > text-ui-test\ACTUAL.TXT

fc /n text-ui-test\EXPECTED.TXT text-ui-test\ACTUAL.TXT > nul
if errorlevel 1 (
  echo FAIL
  popd & exit /b 1
) else (
  echo PASS
  popd & exit /b 0
)
