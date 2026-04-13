-- Sample data for public.mt_city (15 rows)
-- Entity source: City.java; id is server-generated.
-- Assumes public.employees.id = 1 exists for audit columns.

INSERT INTO public.mt_city (created_date, last_modified_date, city_code, city_name, taluka_id, created_by, last_modified_by)
VALUES
(now(), now(), 'PUN', 'Pune', 1, 1, 1),
(now(), now(), 'HOU', 'Houston', 2, 1, 1),
(now(), now(), 'LON', 'London', 3, 1, 1),
(now(), now(), 'TOR', 'Toronto', 4, 1, 1),
(now(), now(), 'SYD', 'Sydney', 5, 1, 1),
(now(), now(), 'STR', 'Stuttgart', 6, 1, 1),
(now(), now(), 'PAR', 'Paris', 7, 1, 1),
(now(), now(), 'TYO', 'Tokyo', 8, 1, 1),
(now(), now(), 'SGP', 'Singapore', 9, 1, 1),
(now(), now(), 'DXB', 'Dubai', 10, 1, 1),
(now(), now(), 'RUH', 'Riyadh', 11, 1, 1),
(now(), now(), 'KTM', 'Kathmandu', 12, 1, 1),
(now(), now(), 'DAC', 'Dhaka', 13, 1, 1),
(now(), now(), 'CMB', 'Colombo', 14, 1, 1),
(now(), now(), 'LHE', 'Lahore', 15, 1, 1);
