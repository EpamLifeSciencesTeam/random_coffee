#!/bin/bash
set -e
psql -v ON_ERROR_STOP=1 --username "postgres" --dbname "coffee_db" <<-EOSQL
  BEGIN;
    CREATE SCHEMA authentication;
  COMMIT;
EOSQL