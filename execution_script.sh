#!/bin/bash

# Try to detect the location of Java
if command -v java &> /dev/null; then
    echo "Java found at $(which java)."
    export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
else
    echo "Java not found. Installing Java 17..."

    # Update package list
    sudo apt update

    # Install Java 17
    sudo apt install -y openjdk-17-jdk

    # Verify installation
    if command -v java &> /dev/null; then
        echo "Java 17 installed successfully."
        export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
    else
        echo "Java installation failed. Please install Java manually and run this script again."
        exit 1
    fi
fi

# Check and install Node.js and npm if not present
if ! command -v npm &> /dev/null; then
    echo "npm not found. Installing Node.js and npm..."

    # Update package list
    sudo apt update

    # Install Node.js and npm
    sudo apt install -y nodejs npm

    # Verify installation
    if command -v npm &> /dev/null; then
        echo "Node.js and npm installed successfully."
    else
        echo "Node.js and npm installation failed. Please install them manually and run this script again."
        exit 1
    fi
else
    echo "npm is already installed."
fi

# Ensure the Maven wrapper has execution permissions
if [ ! -x backend/mvnw ]; then
    echo "Setting execution permissions for Maven wrapper..."
    chmod +x backend/mvnw
fi

# Set environment variables for the backend
export SPRING_DATASOURCE_URL="jdbc:mysql://sql10.freesqldatabase.com:3306/sql10726000"
export SPRING_DATASOURCE_USERNAME="sql10726000"
export SPRING_DATASOURCE_PASSWORD="Blwz7mgTeE"

# Build the backend with Maven
echo "Building the backend (Spring Boot) with Maven..."
(cd backend && ./mvnw clean install)

# Set environment variables for the backend
export SPRING_DATASOURCE_URL="jdbc:mysql://sql10.freesqldatabase.com:3306/sql10726000"
export SPRING_DATASOURCE_USERNAME="sql10726000"
export SPRING_DATASOURCE_PASSWORD="Blwz7mgTeE"

# Initialize the backend database (you can run SQL scripts or use Hibernate)
echo "Initializing the backend database..."
(cd backend && ./mvnw spring-boot:run liquibase:update &)

# Wait for a few seconds to ensure the backend is up and running
sleep 10

# Build the frontend (React) - Install dependencies and build
echo "Building the frontend (React)..."
(cd frontend && npm install && npm install bootstrap sweetalert2 axios && npm run build && npm start)

# Start the complete application
echo "Starting the complete application..."
