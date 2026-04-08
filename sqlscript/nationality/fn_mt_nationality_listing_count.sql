CREATE OR REPLACE FUNCTION public.fn_mt_nationality_listing_count(
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
    FROM mt_nationality n
    WHERE n.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(n.nationality_code) LIKE LOWER(searchText) || '%'
         OR LOWER(n.nationality_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;
