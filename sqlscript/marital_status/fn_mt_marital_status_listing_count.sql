CREATE OR REPLACE FUNCTION public.fn_mt_marital_status_listing_count(
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
    FROM mt_marital_status ms
    WHERE ms.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(ms.marital_status_code) LIKE LOWER(searchText) || '%'
         OR LOWER(ms.marital_status_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;
