CREATE OR REPLACE FUNCTION public.fn_mt_marital_status_dropdown(
)
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT ms.id, ms.id, ms.marital_status_name::text
    FROM mt_marital_status ms
    WHERE ms.delete_flag = false
      AND ms.active = true
    ORDER BY ms.marital_status_name;
END;
$BODY$;
