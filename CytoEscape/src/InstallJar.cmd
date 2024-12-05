@echo off
:: Define the target directory
set TARGET_DIR=%USERPROFILE%\CytoscapeConfiguration\3\apps\installed

:: Check if the target directory exists, and create it if it doesn't
if not exist "%TARGET_DIR%" (
    echo The target directory does not exist. Creating it now...
    mkdir "%TARGET_DIR%"
)

:: Define the source directory (you can change this to the folder containing your .jar files)
set SOURCE_DIR=%CD%\..\target

:: Loop through each .jar file in the source directory
for %%F in ("%SOURCE_DIR%\*.jar") do (
    :: Check if the file already exists in the target directory
    if exist "%TARGET_DIR%\%%~nxF" (
        :: Prompt the user for confirmation
        echo File "%%~nxF" already exists in the target directory.
        set /p OVERWRITE="Do you want to overwrite it? (Y/N): "
        if /i "%OVERWRITE%"=="Y" (
            echo Overwriting "%%~nxF"...
            move /Y "%%F" "%TARGET_DIR%\"
        ) else (
            echo Skipping "%%~nxF"...
        )
    ) else (
        echo Moving "%%~nxF" to the target directory...
        move "%%F" "%TARGET_DIR%\"
    )
)

:: Confirm completion
echo Process completed.
pause
