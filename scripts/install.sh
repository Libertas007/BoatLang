#!/bin/bash

echo "⛵ Installing Boat..."

# Create .boat directory in the user's home directory
boatPath="$HOME/.boat"
if [ ! -d "$boatPath" ]; then
    mkdir "$boatPath"
    echo "📂 Created a ~/.boat/ folder."
fi

# Define the GitHub repository details
apiUrl="https://api.github.com/repos/Libertas007/BoatLang/releases/latest"

# Fetch the latest release information
response=$(curl -sL -H "Accept: application/vnd.github.v3+json" "$apiUrl")
version=$(echo "$response" | jq -r '.tag_name')
echo "📨 Downloading version $version..."

# Download assets
echo "$response" | jq -r '.assets[] | .browser_download_url' | while read -r url; do
    curl -L -o "$boatPath/$(basename $url)" "$url"
done


filePath="$boatPath/version.txt"

# Create the file if it doesn't exist
if [ ! -f "$filePath" ]; then
    touch "$filePath"
fi

# Get the current date in milliseconds since the epoch
dateInMillis=$(($(date +%s%N)/1000000))

# Write the current date in milliseconds to the file
echo "$version;;$dateInMillis" > $filePath


# Get the current PATH environment variable
currentPath=$(echo $PATH | tr ':' '\n')

# Check if the new path is already in the PATH
if ! echo "$currentPath" | grep -q "$boatPath"; then
    echo "export PATH=\$PATH:$boatPath" >> ~/.bashrc
    source ~/.bashrc
    echo "👣 Boat folder added to PATH successfully."
fi

echo "✅ Installation done!"
echo "⛵ Now create your first Boat project:"
echo "  boat new test"
echo "or run the Boat terminal:"
echo "  boat"
