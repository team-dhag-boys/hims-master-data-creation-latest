-- Sample data for public.mt_country (15 rows)
-- Entity source: Country.java; id is server-generated.
-- Assumes public.employees.id = 1 exists for audit columns.

INSERT INTO public.mt_country (created_date, last_modified_date, country_code, country_name, isd_code, mobile_length, created_by, last_modified_by)
VALUES
(now(), now(), 'IN', 'India', '+91', '10', 1, 1),
(now(), now(), 'US', 'United States', '+1', '10', 1, 1),
(now(), now(), 'GB', 'United Kingdom', '+44', '10', 1, 1),
(now(), now(), 'CA', 'Canada', '+120', '10', 1, 1),
(now(), now(), 'AU', 'Australia', '+61', '9', 1, 1),
(now(), now(), 'DE', 'Germany', '+49', '11', 1, 1),
(now(), now(), 'FR', 'France', '+33', '9', 1, 1),
(now(), now(), 'JP', 'Japan', '+81', '10', 1, 1),
(now(), now(), 'SG', 'Singapore', '+65', '8', 1, 1),
(now(), now(), 'AE', 'United Arab Emirates', '+971', '9', 1, 1),
(now(), now(), 'SA', 'Saudi Arabia', '+966', '9', 1, 1),
(now(), now(), 'NP', 'Nepal', '+977', '10', 1, 1),
(now(), now(), 'BD', 'Bangladesh', '+880', '10', 1, 1),
(now(), now(), 'LK', 'Sri Lanka', '+94', '9', 1, 1),
(now(), now(), 'PK', 'Pakistan', '+92', '10', 1, 1);
