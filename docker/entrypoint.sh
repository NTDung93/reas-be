#!/bin/bash

set -e

DB_HOST=postgres
DB_PORT=5432
DB_USER=postgres
DB_PASS=12345678
DB_NAME=reas

echo "Waiting for PostgreSQL to be ready at $DB_HOST:$DB_PORT..."
/wait-for-it.sh $DB_HOST $DB_PORT

export PGPASSWORD=$DB_PASS

echo "Checking available items in database..."
AVAILABLE_ITEMS=$(psql -h $DB_HOST -U $DB_USER -d $DB_NAME -t -c "SELECT COUNT(*) FROM "ITEM" WHERE "STATUS_ITEM" = 'AVAI';" | xargs)

if [ "$AVAILABLE_ITEMS" -eq "0" ]; then
  echo "No available items found. Importing initial data..."
  psql -h $DB_HOST -U $DB_USER -d $DB_NAME -f /initdata.sql
else
  echo "Found $AVAILABLE_ITEMS available items."
fi

echo "Starting Spring Boot application..."
exec java -jar /app/reas-be.jar
