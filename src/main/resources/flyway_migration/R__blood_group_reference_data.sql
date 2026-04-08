-- repeatable_checksum_marker: 20260314_01
-- Maintain canonical blood-group reference rows.
DO $$
BEGIN
    IF to_regclass('public.mt_blood_group') IS NULL THEN
        RETURN;
    END IF;

    INSERT INTO mt_blood_group (blood_group_code, blood_group_name, active, delete_flag, created_date, last_modified_date)
    SELECT 'A+', 'A Positive', true, false, now(), now()
    WHERE NOT EXISTS (
        SELECT 1 FROM mt_blood_group WHERE blood_group_code = 'A+'
    );

    INSERT INTO mt_blood_group (blood_group_code, blood_group_name, active, delete_flag, created_date, last_modified_date)
    SELECT 'A-', 'A Negative', true, false, now(), now()
    WHERE NOT EXISTS (
        SELECT 1 FROM mt_blood_group WHERE blood_group_code = 'A-'
    );

    INSERT INTO mt_blood_group (blood_group_code, blood_group_name, active, delete_flag, created_date, last_modified_date)
    SELECT 'B+', 'B Positive', true, false, now(), now()
    WHERE NOT EXISTS (
        SELECT 1 FROM mt_blood_group WHERE blood_group_code = 'B+'
    );

    INSERT INTO mt_blood_group (blood_group_code, blood_group_name, active, delete_flag, created_date, last_modified_date)
    SELECT 'B-', 'B Negative', true, false, now(), now()
    WHERE NOT EXISTS (
        SELECT 1 FROM mt_blood_group WHERE blood_group_code = 'B-'
    );

    INSERT INTO mt_blood_group (blood_group_code, blood_group_name, active, delete_flag, created_date, last_modified_date)
    SELECT 'AB+', 'AB Positive', true, false, now(), now()
    WHERE NOT EXISTS (
        SELECT 1 FROM mt_blood_group WHERE blood_group_code = 'AB+'
    );

    INSERT INTO mt_blood_group (blood_group_code, blood_group_name, active, delete_flag, created_date, last_modified_date)
    SELECT 'AB-', 'AB Negative', true, false, now(), now()
    WHERE NOT EXISTS (
        SELECT 1 FROM mt_blood_group WHERE blood_group_code = 'AB-'
    );

    INSERT INTO mt_blood_group (blood_group_code, blood_group_name, active, delete_flag, created_date, last_modified_date)
    SELECT 'O+', 'O Positive', true, false, now(), now()
    WHERE NOT EXISTS (
        SELECT 1 FROM mt_blood_group WHERE blood_group_code = 'O+'
    );

    INSERT INTO mt_blood_group (blood_group_code, blood_group_name, active, delete_flag, created_date, last_modified_date)
    SELECT 'O-', 'O Negative', true, false, now(), now()
    WHERE NOT EXISTS (
        SELECT 1 FROM mt_blood_group WHERE blood_group_code = 'O-'
    );

    UPDATE mt_blood_group
    SET active = true,
        delete_flag = false,
        last_modified_date = now()
    WHERE blood_group_code IN ('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-');
END $$;
