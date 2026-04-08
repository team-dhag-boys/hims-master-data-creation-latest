-- Sample data for public.mt_pin_code (15 rows)
-- Assumes public.employees.id = 1 exists for audit columns.

INSERT INTO public.mt_pin_code (created_date, last_modified_date, active, delete_flag, pin_code, created_by, last_modified_by, city_id)
VALUES
(now(), now(), true, false, 411001, 1, 1, 1),
(now(), now(), true, false, 411002, 1, 1, 2),
(now(), now(), true, false, 411003, 1, 1, 3),
(now(), now(), true, false, 411004, 1, 1, 4),
(now(), now(), true, false, 411005, 1, 1, 5),
(now(), now(), true, false, 411006, 1, 1, 6),
(now(), now(), true, false, 411007, 1, 1, 7),
(now(), now(), true, false, 411008, 1, 1, 8),
(now(), now(), true, false, 411009, 1, 1, 9),
(now(), now(), true, false, 411010, 1, 1, 10),
(now(), now(), true, false, 411011, 1, 1, 11),
(now(), now(), true, false, 411012, 1, 1, 12),
(now(), now(), true, false, 411013, 1, 1, 13),
(now(), now(), true, false, 411014, 1, 1, 14),
(now(), now(), true, false, 411015, 1, 1, 15);
