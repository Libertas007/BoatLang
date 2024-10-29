#!/bin/bash

# Check if Java is installed
if ! command -v java &> /dev/null; then
  echo "❌ Java is not installed. Please install Java and try again."
  exit 1
fi

# Check if boat.jar exists in the ~/.boat/ directory
if [ ! -f "$HOME/.boat/boat.jar" ]; then
    echo "❌ The boat.jar file does not exist in the ~/.boat/ directory. Please install it first."
    exit 1
fi

# Run the boat.jar file with all arguments passed to the script
java -jar "$HOME/.boat/boat.jar" "$@"
