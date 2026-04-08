CREATE OR REPLACE FUNCTION public.fn_mt_blood_group_listing_count(
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
    FROM mt_blood_group bg
    WHERE bg.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(bg.blood_group_code) LIKE LOWER(searchText) || '%'
         OR LOWER(bg.blood_group_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;
