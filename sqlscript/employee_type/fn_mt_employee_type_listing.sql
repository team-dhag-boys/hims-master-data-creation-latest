CREATE OR REPLACE FUNCTION public.fn_mt_employee_type_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        employeetype character varying,
        isclinical boolean,
        applicabletodoctortype boolean,
        isdoctor boolean,
        active boolean,
        createddate text
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
        to_char(et.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_employee_type et
    WHERE et.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(et.employee_type) LIKE LOWER(searchText) || '%'
      )
    ORDER BY et.created_date DESC, et.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;
