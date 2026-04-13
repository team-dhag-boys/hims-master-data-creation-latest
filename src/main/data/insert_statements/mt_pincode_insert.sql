-- Sample data for public.mt_pincode (15 rows)
-- Entity source: PinCode.java; id is server-generated.
-- Assumes public.employees.id = 1 exists for audit columns.

INSERT INTO public.mt_pincode (created_date, last_modified_date, pincode, city_id, created_by, last_modified_by)
VALUES
(now(), now(), '411001', 1, 1, 1),
(now(), now(), '77001', 2, 1, 1),
(now(), now(), 'SW1A1AA', 3, 1, 1),
(now(), now(), 'M5H2N2', 4, 1, 1),
(now(), now(), '2000', 5, 1, 1),
(now(), now(), '70173', 6, 1, 1),
(now(), now(), '75001', 7, 1, 1),
(now(), now(), '1000001', 8, 1, 1),
(now(), now(), '018989', 9, 1, 1),
(now(), now(), '00000', 10, 1, 1),
(now(), now(), '12271', 11, 1, 1),
(now(), now(), '44600', 12, 1, 1),
(now(), now(), '1205', 13, 1, 1),
(now(), now(), '00100', 14, 1, 1),
(now(), now(), '54000', 15, 1, 1);
