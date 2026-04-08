CREATE OR REPLACE FUNCTION public.fn_mt_blood_group_autocomplete(
    searchtext text)
    RETURNS TABLE(id bigint, label character varying, value character varying)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        bg.id,
        bg.blood_group_name,
        bg.blood_group_code
    FROM mt_blood_group bg
    WHERE bg.delete_flag = false
      AND bg.active = true
      AND (
            bg.blood_group_code ILIKE '%' || searchText || '%'
         OR bg.blood_group_name ILIKE '%' || searchText || '%'
      )
    ORDER BY bg.blood_group_name;
END;
$BODY$;
