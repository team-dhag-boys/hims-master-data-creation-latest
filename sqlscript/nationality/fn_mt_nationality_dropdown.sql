CREATE OR REPLACE FUNCTION public.fn_mt_nationality_dropdown(
)
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT n.id, n.id, n.nationality_name::text
    FROM mt_nationality n
    WHERE n.delete_flag = false
      AND n.active = true
    ORDER BY n.nationality_name;
END;
$BODY$;
