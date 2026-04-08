CREATE OR REPLACE FUNCTION public.fn_mt_nationality_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
    UPDATE mt_nationality
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
