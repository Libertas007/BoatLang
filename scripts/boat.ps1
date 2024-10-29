# Check if Java is installed
if (-not (Get-Command java -ErrorAction SilentlyContinue))
{
    Write-Output "❌ Java is not installed. Please install Java and try again." Exit
}

# Check if boat.jar exists in the ~/.boat/ directory
if (-not (Test-Path "$env:userprofile\.boat\boat.jar"))
{
    Write-Output "❌ The boat.jar file does not exist in the ~/.boat/ directory. Please install it first."
    Exit
}

# Run the boat.jar file with all arguments passed to the script
& java -jar "$env:userprofile\.boat\boat.jar" $args

