-- Sample data for public.mt_country (15 rows)
-- Assumes public.employees.id = 1 exists for audit columns.

INSERT INTO public.mt_country (created_date, last_modified_date, active, country_code, country_name, delete_flag, is_default, isd_code, mobile_length, created_by, last_modified_by)
VALUES
(now(), now(), true, 'IN', 'India', false, true, '+91', '10', 1, 1),
(now(), now(), true, 'US', 'United States', false, false, '+1', '10', 1, 1),
(now(), now(), true, 'GB', 'United Kingdom', false, false, '+44', '10', 1, 1),
(now(), now(), true, 'CA', 'Canada', false, false, '+120', '10', 1, 1),
(now(), now(), true, 'AU', 'Australia', false, false, '+61', '9', 1, 1),
(now(), now(), true, 'DE', 'Germany', false, false, '+49', '11', 1, 1),
(now(), now(), true, 'FR', 'France', false, false, '+33', '9', 1, 1),
(now(), now(), true, 'JP', 'Japan', false, false, '+81', '10', 1, 1),
(now(), now(), true, 'SG', 'Singapore', false, false, '+65', '8', 1, 1),
(now(), now(), true, 'AE', 'United Arab Emirates', false, false, '+971', '9', 1, 1),
(now(), now(), true, 'SA', 'Saudi Arabia', false, false, '+966', '9', 1, 1),
(now(), now(), true, 'NP', 'Nepal', false, false, '+977', '10', 1, 1),
(now(), now(), true, 'BD', 'Bangladesh', false, false, '+880', '10', 1, 1),
(now(), now(), true, 'LK', 'Sri Lanka', false, false, '+94', '9', 1, 1),
(now(), now(), true, 'PK', 'Pakistan', false, false, '+92', '10', 1, 1);
