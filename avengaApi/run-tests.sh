#!/bin/bash

# Bookstore API Test Automation - Execution Script
# This script provides convenient ways to run the tests

set -e

echo "=========================================="
echo "Bookstore API Test Automation Framework"
echo "=========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_info() {
    echo -e "${BLUE}ℹ${NC} $1"
}

print_success() {
    echo -e "${GREEN}✓${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
}

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    print_warning "Maven is not installed. Please install Maven to run tests."
    print_info "Visit: https://maven.apache.org/install.html"
    exit 1
fi

# Check Java version
if ! command -v java &> /dev/null; then
    print_warning "Java is not installed. Please install Java 11 or higher."
    exit 1
fi

java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
if [ "$java_version" -lt 11 ]; then
    print_warning "Java version is $java_version. Java 11 or higher is required."
    exit 1
fi

print_success "Java $java_version found"
print_success "Maven $(mvn -version | head -n 1) found"
echo ""

# Create necessary directories
mkdir -p test-output logs

# Menu for test execution options
echo "Select an option:"
echo "1) Run all tests"
echo "2) Run only Books API tests"
echo "3) Run only Authors API tests"
echo "4) Run tests with custom base URL"
echo "5) Run tests in Docker"
echo ""
read -p "Enter your choice (1-5): " choice

case $choice in
    1)
        print_info "Running all tests..."
        mvn clean test
        ;;
    2)
        print_info "Running Books API tests..."
        mvn clean test -Dtest=BooksApiTest
        ;;
    3)
        print_info "Running Authors API tests..."
        mvn clean test -Dtest=AuthorsApiTest
        ;;
    4)
        read -p "Enter base URL: " base_url
        print_info "Running tests with base URL: $base_url"
        mvn clean test -DBASE_URL="$base_url"
        ;;
    5)
        print_info "Building Docker image..."
        docker build -t bookstore-api-tests:latest .
        
        print_info "Running tests in Docker container..."
        docker run --rm \
            -e BASE_URL=https://fakerestapi.azurewebsites.net \
            -e ENV=docker \
            -v $(pwd)/test-output:/app/test-output \
            -v $(pwd)/logs:/app/logs \
            bookstore-api-tests:latest
        ;;
    *)
        print_warning "Invalid choice. Exiting..."
        exit 1
        ;;
esac

# Check if reports were generated
if [ -f "test-output/extent-report.html" ]; then
    print_success "Tests completed successfully!"
    print_info "View report: test-output/extent-report.html"
    
    # Try to open the report automatically
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS
        open test-output/extent-report.html
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        # Linux
        xdg-open test-output/extent-report.html 2>/dev/null || echo "Please open test-output/extent-report.html manually"
    fi
else
    print_warning "Test report not found. Tests may have failed."
fi

echo ""
print_info "Logs are available in: logs/api-tests.log"

