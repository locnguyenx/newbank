#!/bin/bash
# start-grepai.sh - Start Ollama and GrepAI watcher for moqui-opencode
# Usage: ./start-grepai.sh

set -e

PROJECT_DIR="/Users/me/myprojects/opencode-superpowers"
GREPAI_BIN="$HOME/.local/bin/grepai"
LOG_FILE="$HOME/.grepai_watch.log"

echo "=== GrepAI Starter ==="

# 1. Check/Start Ollama
echo "Checking Ollama..."
if curl -s http://localhost:11434/api/tags > /dev/null 2>&1; then
    echo "  Ollama is already running"
else
    echo "  Starting Ollama..."
    if [ -d "/Applications/Ollama.app" ]; then
        open -a Ollama 2>/dev/null || /Applications/Ollama.app/Contents/MacOS/ollama serve &
    else
        ollama serve &
    fi
    sleep 3
fi

# Verify Ollama
if curl -s http://localhost:11434/api/tags > /dev/null 2>&1; then
    echo "  Ollama verified"
else
    echo "  ERROR: Ollama failed to start"
    exit 1
fi

# 2. Kill existing grepai watcher
echo "Stopping existing grepai watcher..."
pkill -f "grepai watch" 2>/dev/null || true
sleep 1

# 3. Start GrepAI watcher
echo "Starting GrepAI watcher..."
cd "$PROJECT_DIR"
nohup $GREPAI_BIN watch > "$LOG_FILE" 2>&1 &
sleep 3

# 4. Verify
echo ""
echo "=== GrepAI Status ==="
$GREPAI_BIN status

echo ""
echo "=== Recent Log ==="
tail -10 "$LOG_FILE" 2>/dev/null || echo "No log yet"

echo ""
echo "Done! GrepAI is running."
echo "Log file: $LOG_FILE"
