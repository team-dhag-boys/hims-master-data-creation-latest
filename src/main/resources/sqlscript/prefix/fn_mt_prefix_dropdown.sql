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
select mp.id as prefixId,mp.id as prefixId,mp.prefix_name::text,mp.gender_prefix from mt_prefix mp;
END;
$BODY$;