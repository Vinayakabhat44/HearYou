#!/bin/bash

# Usage: ./upload-story.sh -u username -p password -c "Content" [-f "/path/to/file"] [-t POST] [-lat 12.9] [-lng 77.5]

while [[ $# -gt 0 ]]; do
  case $1 in
    -u) USERNAME="$2"; shift 2 ;;
    -p) PASSWORD="$2"; shift 2 ;;
    -c) CONTENT="$2"; shift 2 ;;
    -f) FILEPATH="$2"; shift 2 ;;
    -t) TYPE="$2"; shift 2 ;;
    -lat) LAT="$2"; shift 2 ;;
    -lng) LNG="$2"; shift 2 ;;
    *) shift ;;
  esac
done

if [ -z "$USERNAME" ] || [ -z "$PASSWORD" ] || [ -z "$CONTENT" ]; then
  echo "Usage: $0 -u username -p password -c 'Content' [-f filepath] [-t type] [-lat lat] [-lng lng]"
  exit 1
fi

GATEWAY_URL="http://localhost:8080"
TYPE=${TYPE:-"POST"}

echo "Logging in as $USERNAME..."
TOKEN=$(curl -s -X POST "$GATEWAY_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"username\": \"$USERNAME\", \"password\": \"$PASSWORD\"}" | jq -r '.token')

if [ "$TOKEN" == "null" ] || [ -z "$TOKEN" ]; then
  echo "Login failed!"
  exit 1
fi

echo "Login successful! Uploading story..."

STORY_JSON="{\"content\": \"$CONTENT\", \"type\": \"$TYPE\"}"

URL="$GATEWAY_URL/api/stories"
PARAMS=""
if [ ! -z "$LAT" ]; then PARAMS="lat=$LAT"; fi
if [ ! -z "$LNG" ]; then 
  if [ ! -z "$PARAMS" ]; then PARAMS="$PARAMS&lng=$LNG"; else PARAMS="lng=$LNG"; fi
fi
if [ ! -z "$PARAMS" ]; then URL="$URL?$PARAMS"; fi

if [ ! -z "$FILEPATH" ] && [ -f "$FILEPATH" ]; then
  curl -X POST "$URL" \
    -H "Authorization: Bearer $TOKEN" \
    -F "story=$STORY_JSON;type=application/json" \
    -F "file=@$FILEPATH"
else
  curl -X POST "$URL" \
    -H "Authorization: Bearer $TOKEN" \
    -F "story=$STORY_JSON;type=application/json"
fi

echo -e "\nDone!"
