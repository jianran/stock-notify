#!/bin/bash
set -e

JAR_PATH="$1"
APP_DIR="/opt/stock-notify"
OLD_JAR="$APP_DIR/stock-notify.jar"
NEW_JAR="$JAR_PATH"

echo "Stopping existing application..."
pkill -f stock-notify || true

sleep 2

echo "Backing up old JAR..."
mv "$OLD_JAR" "${OLD_JAR}.backup" 2>/dev/null || true

echo "Deploying new JAR..."
cp "$NEW_JAR" "$APP_DIR/"

echo "Starting application..."
cd "$APP_DIR"
nohup java -jar stock-notify.jar > app.log 2>&1 &

echo "Waiting for application to start..."
sleep 5

if pgrep -f stock-notify > /dev/null; then
    echo "Application started successfully!"
else
    echo "Application failed to start!"
    exit 1
fi
