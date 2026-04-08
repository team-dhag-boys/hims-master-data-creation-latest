-- Master script: run from this directory, e.g. psql -v ON_ERROR_STOP=1 -f run_all_inserts.sql
-- Requires existing row public.employees(id) = 1.

\set ON_ERROR_STOP on
\echo 'Seeding mt_country...'
\i mt_country_insert.sql

\echo 'Seeding mt_state...'
\i mt_state_insert.sql

\echo 'Seeding mt_district...'
\i mt_district_insert.sql

\echo 'Seeding mt_taluka...'
\i mt_taluka_insert.sql

\echo 'Seeding mt_city...'
\i mt_city_insert.sql

\echo 'Seeding mt_pin_code...'
\i mt_pin_code_insert.sql

\echo 'Seeding mt_area...'
\i mt_area_insert.sql
