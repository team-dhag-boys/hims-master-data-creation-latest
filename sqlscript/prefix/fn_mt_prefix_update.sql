CREATE OR REPLACE FUNCTION public.fn_mt_prefix_update(
	pid bigint,
	pprefixcode character varying,
	pprefixname character varying,
	pactive boolean,
	pgenderprefix bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
    UPDATE mt_prefix
    SET prefix_code = pPrefixCode,
        prefix_name = pPrefixName,
        active = pActive,
        gender_prefix = pGenderPrefix
    WHERE id = pId;
END;
$BODY$;