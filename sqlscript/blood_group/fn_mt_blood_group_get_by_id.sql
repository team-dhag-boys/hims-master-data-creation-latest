CREATE OR REPLACE FUNCTION public.fn_mt_blood_group_get_by_id(
    pbloodgroupid bigint)
    RETURNS TABLE(
        id bigint,
        bloodgroupcode character varying,
        bloodgroupname character varying,
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
        bg.id,
        bg.blood_group_code,
        bg.blood_group_name,
        bg.active,
        bg.delete_flag,
        to_char(bg.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(bg.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_blood_group bg
    WHERE bg.id = pBloodGroupId
      AND bg.delete_flag = false;
END;
$BODY$;
