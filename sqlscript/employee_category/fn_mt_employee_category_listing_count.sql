CREATE OR REPLACE FUNCTION public.fn_mt_employee_category_listing_count(
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT count(*)
    FROM mt_employee_category ec
    LEFT JOIN mt_employee_type et
           ON et.id = ec.employee_type_id
    WHERE ec.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(ec.category) LIKE LOWER(searchText) || '%'
         OR LOWER(ec.code) LIKE LOWER(searchText) || '%'
         OR LOWER(COALESCE(et.employee_type, '')) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;
