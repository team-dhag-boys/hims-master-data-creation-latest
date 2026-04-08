CREATE OR REPLACE FUNCTION public.fn_mt_employee_category_get_by_id(
    pemployeecategoryid bigint)
    RETURNS TABLE(
        id bigint,
        category character varying,
        code character varying,
        employeetypeid bigint,
        employeetype character varying,
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
        ec.id,
        ec.category,
        ec.code,
        et.id,
        et.employee_type,
        ec.active,
        ec.delete_flag,
        to_char(ec.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(ec.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_employee_category ec
    LEFT JOIN mt_employee_type et
           ON et.id = ec.employee_type_id
    WHERE ec.id = pEmployeeCategoryId
      AND ec.delete_flag = false;
END;
$BODY$;
