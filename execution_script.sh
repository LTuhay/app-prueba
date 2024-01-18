#!/bin/bash

# Try to detect the location of Java
if command -v java &> /dev/null; then
    export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
else
    echo "Java not found. Please install Java and run this script again."
    exit 1
fi

# Default username and password for MySQL database
DEFAULT_DB_USER=notes_user
DEFAULT_DB_PASSWORD=123456

# Check if MySQL is installed
if ! command -v mysql &> /dev/null; then
    echo "MySQL is not installed. Please install MySQL manually and run this script again."
    exit 1
fi

# Create MySQL user if it doesn't exist
echo "Creating MySQL user..."
sudo mysql -uroot -p <<EOF
CREATE DATABASE IF NOT EXISTS notes;
CREATE USER IF NOT EXISTS '$DEFAULT_DB_USER'@'localhost' IDENTIFIED BY '$DEFAULT_DB_PASSWORD';
GRANT ALL PRIVILEGES ON notes.* TO '$DEFAULT_DB_USER'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;
EOF

# Build the backend with Maven
echo "Building the backend (Spring Boot) with Maven..."
(cd backend && ./mvnw clean install)

# Set environment variables for the backend (Spring Boot)
export SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/notes"
export SPRING_DATASOURCE_USERNAME=$DEFAULT_DB_USER
export SPRING_DATASOURCE_PASSWORD=$DEFAULT_DB_PASSWORD

# Initialize the database (you can run SQL scripts or use Hibernate)
echo "Initializing the backend database..."
(cd backend && ./mvnw spring-boot:run liquibase:update &)

# Wait for a few seconds to ensure the backend is up and running
sleep 10

# Build the frontend (React) - Install dependencies and build
echo "Building the frontend (React)..."
(cd frontend && npm install && npm install bootstrap sweetalert2 && npm install axios && npm run build && npm start)

# Start the complete application
echo "Starting the complete application..."
