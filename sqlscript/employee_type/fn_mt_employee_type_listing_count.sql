CREATE OR REPLACE FUNCTION public.fn_mt_employee_type_listing_count(
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
    FROM mt_employee_type et
    WHERE et.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(et.employee_type) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;
