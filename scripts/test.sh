#!/bin/bash

echo "Testing Java Shopping Application..."

response=$(curl -s http://localhost:8080/health)

if [ "$response" = "UP" ]; then
    echo "Test passed!"
else
    echo "Test failed!"
    echo "Expected: UP"
    echo "Received: $response"
    exit 1
fi
