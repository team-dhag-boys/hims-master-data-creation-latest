CREATE OR REPLACE FUNCTION public.fn_mt_marital_status_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        maritalstatuscode character varying,
        maritalstatusname character varying,
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
        ms.id,
        ms.marital_status_code,
        ms.marital_status_name,
        ms.active,
        to_char(ms.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_marital_status ms
    WHERE ms.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(ms.marital_status_code) LIKE LOWER(searchText) || '%'
         OR LOWER(ms.marital_status_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY ms.created_date DESC, ms.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;
