CREATE OR REPLACE FUNCTION public.fn_mt_blood_group_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        bloodgroupcode character varying,
        bloodgroupname character varying,
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
        bg.id,
        bg.blood_group_code,
        bg.blood_group_name,
        bg.active,
        to_char(bg.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_blood_group bg
    WHERE bg.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(bg.blood_group_code) LIKE LOWER(searchText) || '%'
         OR LOWER(bg.blood_group_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY bg.created_date DESC, bg.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;
