CREATE OR REPLACE FUNCTION public.fn_mt_prefix_listing_count(
	searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        count(*)
    FROM mt_prefix p
    LEFT JOIN mt_gender g
        ON g.id = p.gender_prefix
    WHERE p.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(p.prefix_code) LIKE LOWER(searchText) || '%'
         OR LOWER(p.prefix_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;
