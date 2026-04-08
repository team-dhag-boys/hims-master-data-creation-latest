-- Sample data for public.mt_district (15 rows)
-- Assumes public.employees.id = 1 exists for audit columns.

INSERT INTO public.mt_district (created_date, last_modified_date, active, delete_flag, district_code, district_name, created_by, last_modified_by, state_id)
VALUES
(now(), now(), true, false, 'PUNE', 'Pune', 1, 1, 1),
(now(), now(), true, false, 'MUM', 'Mumbai Suburban', 1, 1, 2),
(now(), now(), true, false, 'HOU', 'Houston', 1, 1, 4),
(now(), now(), true, false, 'BLR', 'Bengaluru Urban', 1, 1, 3),
(now(), now(), true, false, 'AMD', 'Ahmedabad', 1, 1, 4),
(now(), now(), true, false, 'ERN', 'Ernakulam', 1, 1, 5),
(now(), now(), true, false, 'CHN', 'Chennai', 1, 1, 6),
(now(), now(), true, false, 'NDL', 'New Delhi', 1, 1, 7),
(now(), now(), true, false, 'LKO', 'Lucknow', 1, 1, 8),
(now(), now(), true, false, 'KOL', 'Kolkata', 1, 1, 9),
(now(), now(), true, false, 'LAX', 'Los Angeles', 1, 1, 10),
(now(), now(), true, false, 'BRK', 'Brooklyn', 1, 1, 11),
(now(), now(), true, false, 'LND', 'London', 1, 1, 12),
(now(), now(), true, false, 'EDB', 'Edinburgh', 1, 1, 13),
(now(), now(), true, false, 'BNE', 'Brisbane', 1, 1, 14);
