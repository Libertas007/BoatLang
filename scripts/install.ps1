Write-Host "⛵ Installing Boat..."

# Create .boat directory in the user's home directory
$boatPath = "$env:USERPROFILE\.boat"
if (-not (Test-Path -Path $boatPath))
{
    New-Item -ItemType Directory -Path $boatPath
    Write-Host -ForegroundColor Gray "📂 Created a ~/.boat/ folder."
}

# Define the GitHub repository details
$apiUrl = "https://api.github.com/repos/Libertas007/BoatLang/releases/latest"

# Fetch the latest release information
$response = Invoke-RestMethod -Uri $apiUrl -Headers @{ "User-Agent" = "PowerShell" }

$version = $response.tag_name

Write-Host -ForegroundColor Gray "📨 Downloading version $version..."

# Download assets
foreach ($asset in $response.assets)
{
    Invoke-WebRequest -Uri $asset.browser_download_url -OutFile "$boatPath\$( $asset.name )"
}

# Get the current PATH environment variable
$currentPath = [System.Environment]::GetEnvironmentVariable("PATH", [System.EnvironmentVariableTarget]::User)

# Check if the new path is already in the PATH
if (-not ($currentPath -split ";") -contains $boatPath)
{
    # If not, add it
    $boatPath += ";"
    $updatedPath = $currentPath + $boatPath
    [System.Environment]::SetEnvironmentVariable("PATH", $updatedPath, [System.EnvironmentVariableTarget]::User)
    Write-Host -ForegroundColor Gray "👣 Boat folder added to PATH successfully."
}

Write-Host -ForegroundColor Green "✅ Installation done!\n"
Write-Host -ForegroundColor Gray "⛵ Now create your first Boat project:"
Write-Host -ForegroundColor White "  boat new test"
Write-Host -ForegroundColor Gray "or run the Boat terminal:"
Write-Host -ForegroundColor White "  boat"
