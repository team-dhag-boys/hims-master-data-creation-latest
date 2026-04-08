CREATE OR REPLACE FUNCTION public.fn_mt_marital_status_update(
    pid bigint,
    pcode character varying,
    pname character varying,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
    UPDATE mt_marital_status
    SET marital_status_code = pCode,
        marital_status_name = pName,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
