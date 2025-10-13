@echo off
echo ========================================
echo   ZodiacAI Finder - GitHub Push Script
echo ========================================
echo.

REM Check if git is initialized
if not exist ".git" (
    echo Initializing Git repository...
    git init
    echo.
)

REM Add all files
echo Adding all files to Git...
git add .
echo.

REM Show status
echo Current Git status:
git status
echo.

REM Commit with message
set /p commit_msg="Enter commit message (or press Enter for default): "
if "%commit_msg%"=="" set commit_msg="Update: Added screenshots and completed all features"

echo.
echo Committing with message: %commit_msg%
git commit -m "%commit_msg%"
echo.

REM Check if remote exists
git remote -v | findstr "origin" >nul
if errorlevel 1 (
    echo No remote repository found.
    echo.
    echo Please add your GitHub repository URL:
    echo Example: https://github.com/kreggscode/Zodiac-Ai-Kotlin.git
    echo.
    set /p repo_url="Enter repository URL: "
    
    if not "!repo_url!"=="" (
        echo Adding remote repository...
        git remote add origin !repo_url!
        echo.
    ) else (
        echo No repository URL provided. Exiting...
        pause
        exit /b 1
    )
)

REM Set main branch
echo Setting branch to main...
git branch -M main
echo.

REM Push to GitHub
echo Pushing to GitHub...
git push -u origin main
echo.

if errorlevel 1 (
    echo.
    echo ========================================
    echo   Push failed! Common solutions:
    echo ========================================
    echo.
    echo 1. If authentication failed:
    echo    - Use GitHub Personal Access Token
    echo    - Go to: GitHub Settings ^> Developer settings ^> Personal access tokens
    echo    - Generate new token with 'repo' permissions
    echo    - Use token as password when prompted
    echo.
    echo 2. If repository doesn't exist:
    echo    - Create repository on GitHub first
    echo    - Then run this script again
    echo.
    echo 3. If branch conflicts:
    echo    - Try: git pull origin main --rebase
    echo    - Then run this script again
    echo.
    pause
    exit /b 1
)

echo.
echo ========================================
echo   SUCCESS! Pushed to GitHub!
echo ========================================
echo.
echo Your ZodiacAI Finder app is now on GitHub!
echo Check your repository to see the screenshots.
echo.
pause
