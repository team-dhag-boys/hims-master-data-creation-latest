CREATE OR REPLACE FUNCTION public.fn_mt_nationality_get_by_id(
    pnationalityid bigint)
    RETURNS TABLE(
        id bigint,
        nationalitycode character varying,
        nationalityname character varying,
        active boolean,
        deleteflag boolean,
        createddate text,
        lastmodifieddate text
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
        n.delete_flag,
        to_char(n.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(n.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_nationality n
    WHERE n.id = pNationalityId
      AND n.delete_flag = false;
END;
$BODY$;
