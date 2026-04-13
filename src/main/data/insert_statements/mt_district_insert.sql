-- Sample data for public.mt_district (15 rows)
-- Entity source: District.java; id is server-generated.
-- Assumes public.employees.id = 1 exists for audit columns.

INSERT INTO public.mt_district (created_date, last_modified_date, district_code, district_name, state_id, created_by, last_modified_by)
VALUES
(now(), now(), 'PUN', 'Pune District', 1, 1, 1),
(now(), now(), 'HCO', 'Harris County', 2, 1, 1),
(now(), now(), 'GLN', 'Greater London', 3, 1, 1),
(now(), now(), 'TOR', 'Toronto District', 4, 1, 1),
(now(), now(), 'SYD', 'Sydney District', 5, 1, 1),
(now(), now(), 'STM', 'Stuttgart District', 6, 1, 1),
(now(), now(), 'PAR', 'Paris District', 7, 1, 1),
(now(), now(), 'TKC', 'Tokyo Central', 8, 1, 1),
(now(), now(), 'SGC', 'Singapore Central', 9, 1, 1),
(now(), now(), 'DBD', 'Dubai District', 10, 1, 1),
(now(), now(), 'RYD', 'Riyadh District', 11, 1, 1),
(now(), now(), 'KTM', 'Kathmandu District', 12, 1, 1),
(now(), now(), 'DHK', 'Dhaka District', 13, 1, 1),
(now(), now(), 'CMB', 'Colombo District', 14, 1, 1),
(now(), now(), 'LHR', 'Lahore District', 15, 1, 1);
