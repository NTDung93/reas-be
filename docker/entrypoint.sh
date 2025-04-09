#!/bin/bash

set -e

# Thông tin kết nối cố định
DB_HOST=18.143.78.130
DB_PORT=5432
DB_USER=postgres
DB_PASS=12345678
DB_NAME=reas

echo "Waiting for PostgreSQL to be ready at $DB_HOST:$DB_PORT..."
/wait-for-it.sh $DB_HOST $DB_PORT

echo "Checking available item count in database..."

export PGPASSWORD=$DB_PASS
AVAILABLE_ITEMS=$(psql -h $DB_HOST -U $DB_USER -d $DB_NAME -t -c "SELECT COUNT(*) FROM item WHERE status = 'AVAILABLE';" | xargs)

if [ "$AVAILABLE_ITEMS" -gt "0" ]; then
  echo "Found $AVAILABLE_ITEMS available items. Starting Spring Boot app..."
else
  echo "No available items found. Application will still start."
fi

exec java -jar /app/reas-be.jar
