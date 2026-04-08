-- Sample data for public.mt_city (15 rows)
-- Assumes public.employees.id = 1 exists for audit columns.

INSERT INTO public.mt_city (created_date, last_modified_date, active, city_code, city_name, delete_flag, created_by, last_modified_by, taluka_id, state_id)
VALUES
(now(), now(), true, 'PUN', 'Pune', false, 1, 1, 1, 1),
(now(), now(), true, 'MUM', 'Mumbai', false, 1, 1, 2, 2),
(now(), now(), true, 'HOU_C', 'Houston', false, 1, 1, 3, 4),
(now(), now(), true, 'BLR_C', 'Bengaluru', false, 1, 1, 4, 3),
(now(), now(), true, 'AMD_C', 'Ahmedabad', false, 1, 1, 5, 4),
(now(), now(), true, 'KOC', 'Kochi', false, 1, 1, 6, 5),
(now(), now(), true, 'CHE', 'Chennai', false, 1, 1, 7, 6),
(now(), now(), true, 'DEL', 'New Delhi', false, 1, 1, 8, 7),
(now(), now(), true, 'LKO_CITY', 'Lucknow', false, 1, 1, 9, 8),
(now(), now(), true, 'KOL_C', 'Kolkata', false, 1, 1, 10, 9),
(now(), now(), true, 'LA', 'Los Angeles', false, 1, 1, 11, 10),
(now(), now(), true, 'NYC', 'New York City', false, 1, 1, 12, 11),
(now(), now(), true, 'LON', 'London', false, 1, 1, 13, 12),
(now(), now(), true, 'EDI', 'Edinburgh', false, 1, 1, 14, 13),
(now(), now(), true, 'BNE_C', 'Brisbane', false, 1, 1, 15, 14);
