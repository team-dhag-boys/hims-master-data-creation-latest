CREATE OR REPLACE FUNCTION public.fn_mt_employee_type_get_by_id(
    pemployeetypeid bigint)
    RETURNS TABLE(
        id bigint,
        employeetype character varying,
        isclinical boolean,
        applicabletodoctortype boolean,
        isdoctor boolean,
        active boolean,
        deleteflag boolean,
        createddate text,
        lastmodifieddate text
    )
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        et.id,
        et.employee_type,
        et.is_clinical,
        et.applicable_to_doctor_type,
        et.is_doctor,
        et.active,
        et.delete_flag,
        to_char(et.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(et.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_employee_type et
    WHERE et.id = pEmployeeTypeId
      AND et.delete_flag = false;
END;
$BODY$;
