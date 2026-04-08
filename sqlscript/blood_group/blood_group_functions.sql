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

CREATE OR REPLACE FUNCTION public.fn_mt_blood_group_autocomplete(
    searchtext text)
    RETURNS TABLE(id bigint, label character varying, value character varying)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        bg.id,
        bg.blood_group_name,
        bg.blood_group_code
    FROM mt_blood_group bg
    WHERE bg.delete_flag = false
      AND bg.active = true
      AND (
            bg.blood_group_code ILIKE '%' || searchText || '%'
         OR bg.blood_group_name ILIKE '%' || searchText || '%'
      )
    ORDER BY bg.blood_group_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_blood_group_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        bloodgroupcode character varying,
        bloodgroupname character varying,
        active boolean,
        createddate text
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
        to_char(bg.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_blood_group bg
    WHERE bg.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(bg.blood_group_code) LIKE LOWER(searchText) || '%'
         OR LOWER(bg.blood_group_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY bg.created_date DESC, bg.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_blood_group_listing_count(
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT count(*)
    FROM mt_blood_group bg
    WHERE bg.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(bg.blood_group_code) LIKE LOWER(searchText) || '%'
         OR LOWER(bg.blood_group_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_blood_group_update(
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
    UPDATE mt_blood_group
    SET blood_group_code = pCode,
        blood_group_name = pName,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_blood_group_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
    UPDATE mt_blood_group
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
