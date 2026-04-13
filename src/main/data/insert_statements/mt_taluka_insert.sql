-- Sample data for public.mt_taluka (15 rows)
-- Entity source: Taluka.java; id is server-generated.
-- Assumes public.employees.id = 1 exists for audit columns.

INSERT INTO public.mt_taluka (created_date, last_modified_date, taluka_code, taluka_name, district_id, created_by, last_modified_by)
VALUES
(now(), now(), 'HVL', 'Haveli', 1, 1, 1),
(now(), now(), 'SPR', 'Spring', 2, 1, 1),
(now(), now(), 'WST', 'Westminster', 3, 1, 1),
(now(), now(), 'ETY', 'Etobicoke', 4, 1, 1),
(now(), now(), 'PRM', 'Parramatta', 5, 1, 1),
(now(), now(), 'MTT', 'Mitte', 6, 1, 1),
(now(), now(), 'LDF', 'La Défense', 7, 1, 1),
(now(), now(), 'CHY', 'Chiyoda', 8, 1, 1),
(now(), now(), 'DWT', 'Downtown', 9, 1, 1),
(now(), now(), 'DER', 'Deira', 10, 1, 1),
(now(), now(), 'OLY', 'Olaya', 11, 1, 1),
(now(), now(), 'LTP', 'Lalitpur', 12, 1, 1),
(now(), now(), 'DMD', 'Dhanmondi', 13, 1, 1),
(now(), now(), 'FRT', 'Fort', 14, 1, 1),
(now(), now(), 'MDL', 'Model Town', 15, 1, 1);
