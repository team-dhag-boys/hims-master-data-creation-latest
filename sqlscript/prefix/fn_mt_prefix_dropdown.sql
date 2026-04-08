CREATE OR REPLACE FUNCTION public.fn_mt_prefix_dropdown(
	)
    RETURNS TABLE(id bigint, value bigint, label text, genderid bigint)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
begin
return query
select
    mp.id as id,
    mp.id as value,
    mp.prefix_name::text as label,
    mp.gender_prefix as genderid
from mt_prefix mp
where mp.delete_flag = false
  and mp.active = true;
END;
$BODY$;