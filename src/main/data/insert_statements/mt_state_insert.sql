-- Sample data for public.mt_state (15 rows)
-- Entity source: State.java; id is server-generated.
-- Assumes public.employees.id = 1 exists for audit columns.

INSERT INTO public.mt_state (created_date, last_modified_date, state_code, state_name, country_id, created_by, last_modified_by)
VALUES
(now(), now(), 'MH', 'Maharashtra', 1, 1, 1),
(now(), now(), 'TX', 'Texas', 2, 1, 1),
(now(), now(), 'ENG', 'England', 3, 1, 1),
(now(), now(), 'ON', 'Ontario', 4, 1, 1),
(now(), now(), 'NSW', 'New South Wales', 5, 1, 1),
(now(), now(), 'BW', 'Baden-Württemberg', 6, 1, 1),
(now(), now(), 'IDF', 'Île-de-France', 7, 1, 1),
(now(), now(), 'TK', 'Tokyo Prefecture', 8, 1, 1),
(now(), now(), 'SG', 'Singapore Region', 9, 1, 1),
(now(), now(), 'DXB', 'Dubai Emirate', 10, 1, 1),
(now(), now(), 'RUH', 'Riyadh Province', 11, 1, 1),
(now(), now(), 'BAG', 'Bagmati', 12, 1, 1),
(now(), now(), 'DHK', 'Dhaka Division', 13, 1, 1),
(now(), now(), 'WP', 'Western Province', 14, 1, 1),
(now(), now(), 'PB', 'Punjab', 15, 1, 1);
