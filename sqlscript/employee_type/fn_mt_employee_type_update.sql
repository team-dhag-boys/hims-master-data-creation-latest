CREATE OR REPLACE FUNCTION public.fn_mt_employee_type_update(
    pid bigint,
    pemployeetype character varying,
    pisclinical boolean,
    papplicabletodoctortype boolean,
    pisdoctor boolean,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
    UPDATE mt_employee_type
    SET employee_type = pEmployeeType,
        is_clinical = pIsClinical,
        applicable_to_doctor_type = pApplicableToDoctorType,
        is_doctor = pIsDoctor,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
