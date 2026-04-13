-- Master script generated from common entities
-- Entity path: C:\Users\ADMIN\IdeaProjects\hims-master-data-creation-latest\src\main\java\com\hims\masters\common\entity
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

\echo 'Seeding mt_pincode...'
\i mt_pincode_insert.sql

\echo 'Seeding mt_area...'
\i mt_area_insert.sql
