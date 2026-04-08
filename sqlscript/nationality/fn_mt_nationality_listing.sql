CREATE OR REPLACE FUNCTION public.fn_mt_nationality_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        nationalitycode character varying,
        nationalityname character varying,
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
        n.id,
        n.nationality_code,
        n.nationality_name,
        n.active,
        to_char(n.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_nationality n
    WHERE n.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(n.nationality_code) LIKE LOWER(searchText) || '%'
         OR LOWER(n.nationality_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY n.created_date DESC, n.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;
