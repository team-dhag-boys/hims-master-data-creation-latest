CREATE OR REPLACE FUNCTION public.fn_mt_blood_group_dropdown(
)
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT bg.id, bg.id, bg.blood_group_name::text
    FROM mt_blood_group bg
    WHERE bg.delete_flag = false
      AND bg.active = true
    ORDER BY bg.blood_group_name;
END;
$BODY$;
