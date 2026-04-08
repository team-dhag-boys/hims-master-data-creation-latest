CREATE OR REPLACE FUNCTION public.fn_mt_marital_status_get_by_id(
    pmaritalstatusid bigint)
    RETURNS TABLE(
        id bigint,
        maritalstatuscode character varying,
        maritalstatusname character varying,
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
        ms.id,
        ms.marital_status_code,
        ms.marital_status_name,
        ms.active,
        ms.delete_flag,
        to_char(ms.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(ms.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_marital_status ms
    WHERE ms.id = pMaritalStatusId
      AND ms.delete_flag = false;
END;
$BODY$;
