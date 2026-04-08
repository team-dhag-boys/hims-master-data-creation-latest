-- Sample data for public.mt_area (15 rows)
-- Assumes public.employees.id = 1 exists for audit columns.

INSERT INTO public.mt_area (created_date, last_modified_date, active, area, delete_flag, created_by, last_modified_by, pincode_id)
VALUES
(now(), now(), true, 'Kothrud', false, 1, 1, 1),
(now(), now(), true, 'Andheri West', false, 1, 1, 2),
(now(), now(), true, 'Downtown Houston', false, 1, 1, 3),
(now(), now(), true, 'Indiranagar', false, 1, 1, 4),
(now(), now(), true, 'Navrangpura', false, 1, 1, 5),
(now(), now(), true, 'Marine Drive Kochi', false, 1, 1, 6),
(now(), now(), true, 'T Nagar', false, 1, 1, 7),
(now(), now(), true, 'Connaught Place', false, 1, 1, 8),
(now(), now(), true, 'Hazratganj', false, 1, 1, 9),
(now(), now(), true, 'Salt Lake', false, 1, 1, 10),
(now(), now(), true, 'Santa Monica', false, 1, 1, 11),
(now(), now(), true, 'Brooklyn Heights', false, 1, 1, 12),
(now(), now(), true, 'Westminster Abbey Area', false, 1, 1, 13),
(now(), now(), true, 'Old Town', false, 1, 1, 14),
(now(), now(), true, 'Surfers Paradise', false, 1, 1, 15);
