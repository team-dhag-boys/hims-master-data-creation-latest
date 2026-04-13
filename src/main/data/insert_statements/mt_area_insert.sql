-- Sample data for public.mt_area (15 rows)
-- Entity source: Area.java; id is server-generated.
-- Assumes public.employees.id = 1 exists for audit columns.

INSERT INTO public.mt_area (created_date, last_modified_date, area_code, area_name, pincode_id, created_by, last_modified_by)
VALUES
(now(), now(), 'KOT', 'Kothrud', 1, 1, 1),
(now(), now(), 'DWT', 'Downtown', 2, 1, 1),
(now(), now(), 'WMS', 'Westminster', 3, 1, 1),
(now(), now(), 'DWTN', 'Downtown Toronto', 4, 1, 1),
(now(), now(), 'CBD', 'Sydney CBD', 5, 1, 1),
(now(), now(), 'MIT', 'Mitte Center', 6, 1, 1),
(now(), now(), 'LDF', 'La Défense Hub', 7, 1, 1),
(now(), now(), 'AKB', 'Akihabara', 8, 1, 1),
(now(), now(), 'MRN', 'Marina Bay', 9, 1, 1),
(now(), now(), 'BJR', 'Burj Area', 10, 1, 1),
(now(), now(), 'OLY', 'Olaya Business', 11, 1, 1),
(now(), now(), 'THM', 'Thamel', 12, 1, 1),
(now(), now(), 'GLS', 'Gulshan', 13, 1, 1),
(now(), now(), 'SLV', 'Slave Island', 14, 1, 1),
(now(), now(), 'GUL', 'Gulberg', 15, 1, 1);
