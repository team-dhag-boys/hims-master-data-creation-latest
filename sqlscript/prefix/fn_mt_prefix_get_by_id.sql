CREATE OR REPLACE FUNCTION public.fn_mt_prefix_get_by_id(
	_prefixid bigint)
    RETURNS TABLE(prefixid bigint, prefixcode character varying, prefixname character varying, genderid bigint, gendername character varying, active boolean, deleteflag boolean, createddate text, lastmodifieddate text)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        p.id,
        p.prefix_code,
        p.prefix_name,
        g.id,
        g.gender_name,
        p.active,
        p.delete_flag,
        to_char(p.created_date, 'DD-MM-YYYY HH24:MI:SS'),
 		to_char(p.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
 FROM mt_prefix p
    LEFT JOIN mt_gender g
        ON g.id = p.gender_prefix
    WHERE p.id = _prefixId
      AND p.delete_flag = false;
END;
$BODY$;
