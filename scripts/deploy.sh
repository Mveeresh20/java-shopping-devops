#!/bin/bash

echo "Deploying Java Shopping Application..."

# Stop any existing application
pkill -f "java -jar target/java-shopping-devops-1.0.0.jar" 2>/dev/null

# Start the application in background
nohup java -jar target/java-shopping-devops-1.0.0.jar > app.log 2>&1 &

echo "Application started."
echo "Logs: app.log"
echo "Application: http://localhost:8080/"
