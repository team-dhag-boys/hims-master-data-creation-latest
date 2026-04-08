-- Sample data for public.mt_state (15 rows)
-- Assumes public.employees.id = 1 exists for audit columns.

INSERT INTO public.mt_state (created_date, last_modified_date, active, delete_flag, state_code, state_name, created_by, last_modified_by, country_id)
VALUES
(now(), now(), true, false, 'MH', 'Maharashtra', 1, 1, 1),
(now(), now(), true, false, 'TX', 'Texas', 1, 1, 2),
(now(), now(), true, false, 'KA', 'Karnataka', 1, 1, 1),
(now(), now(), true, false, 'GJ', 'Gujarat', 1, 1, 1),
(now(), now(), true, false, 'KL', 'Kerala', 1, 1, 1),
(now(), now(), true, false, 'TN', 'Tamil Nadu', 1, 1, 1),
(now(), now(), true, false, 'DL', 'Delhi', 1, 1, 1),
(now(), now(), true, false, 'UP', 'Uttar Pradesh', 1, 1, 1),
(now(), now(), true, false, 'WB', 'West Bengal', 1, 1, 1),
(now(), now(), true, false, 'CA', 'California', 1, 1, 2),
(now(), now(), true, false, 'NY', 'New York', 1, 1, 2),
(now(), now(), true, false, 'ENG', 'England', 1, 1, 3),
(now(), now(), true, false, 'SCT', 'Scotland', 1, 1, 3),
(now(), now(), true, false, 'QLD', 'Queensland', 1, 1, 4),
(now(), now(), true, false, 'NSW', 'New South Wales', 1, 1, 4);
