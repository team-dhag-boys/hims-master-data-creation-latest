CREATE OR REPLACE FUNCTION public.fn_mt_employee_category_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        category character varying,
        code character varying,
        employeetypeid bigint,
        employeetype character varying,
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
        ec.id,
        ec.category,
        ec.code,
        et.id,
        et.employee_type,
        ec.active,
        to_char(ec.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_employee_category ec
    LEFT JOIN mt_employee_type et
           ON et.id = ec.employee_type_id
    WHERE ec.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(ec.category) LIKE LOWER(searchText) || '%'
         OR LOWER(ec.code) LIKE LOWER(searchText) || '%'
         OR LOWER(COALESCE(et.employee_type, '')) LIKE LOWER(searchText) || '%'
      )
    ORDER BY ec.created_date DESC, ec.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;
