-- Sample data for public.mt_taluka (15 rows)
-- Assumes public.employees.id = 1 exists for audit columns.

INSERT INTO public.mt_taluka (created_date, last_modified_date, active, delete_flag, taluka_code, taluka_name, created_by, last_modified_by, district_id)
VALUES
(now(), now(), true, false, 'HVL', 'Haveli', 1, 1, 1),
(now(), now(), true, false, 'BWD', 'Borivali', 1, 1, 2),
(now(), now(), true, false, 'SPR', 'Spring', 1, 1, 3),
(now(), now(), true, false, 'WHF', 'Whitefield', 1, 1, 4),
(now(), now(), true, false, 'NAR', 'Naroda', 1, 1, 5),
(now(), now(), true, false, 'KCH', 'Kochi City', 1, 1, 6),
(now(), now(), true, false, 'TMP', 'Tambaram', 1, 1, 7),
(now(), now(), true, false, 'DWK', 'Dwarka', 1, 1, 8),
(now(), now(), true, false, 'LKO_C', 'Lucknow City', 1, 1, 9),
(now(), now(), true, false, 'HOW', 'Howrah', 1, 1, 10),
(now(), now(), true, false, 'OCN', 'Ocean', 1, 1, 11),
(now(), now(), true, false, 'MNH', 'Manhattan', 1, 1, 12),
(now(), now(), true, false, 'WSM', 'Westminster', 1, 1, 13),
(now(), now(), true, false, 'LEH', 'Leith', 1, 1, 14),
(now(), now(), true, false, 'GC', 'Gold Coast', 1, 1, 15);
