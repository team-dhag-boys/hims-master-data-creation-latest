-- repeatable_checksum_marker: 20260313_01
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

CREATE OR REPLACE FUNCTION public.fn_mt_nationality_get_by_id(
    pnationalityid bigint)
    RETURNS TABLE(
        id bigint,
        nationalitycode character varying,
        nationalityname character varying,
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
        n.id,
        n.nationality_code,
        n.nationality_name,
        n.active,
        n.delete_flag,
        to_char(n.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(n.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_nationality n
    WHERE n.id = pNationalityId
      AND n.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_nationality_autocomplete(
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
        n.id,
        n.nationality_name,
        n.nationality_code
    FROM mt_nationality n
    WHERE n.delete_flag = false
      AND n.active = true
      AND (
            n.nationality_code ILIKE '%' || searchText || '%'
         OR n.nationality_name ILIKE '%' || searchText || '%'
      )
    ORDER BY n.nationality_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_nationality_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        nationalitycode character varying,
        nationalityname character varying,
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
        n.id,
        n.nationality_code,
        n.nationality_name,
        n.active,
        to_char(n.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_nationality n
    WHERE n.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(n.nationality_code) LIKE LOWER(searchText) || '%'
         OR LOWER(n.nationality_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY n.created_date DESC, n.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_nationality_listing_count(
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
    FROM mt_nationality n
    WHERE n.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(n.nationality_code) LIKE LOWER(searchText) || '%'
         OR LOWER(n.nationality_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_nationality_update(
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
    UPDATE mt_nationality
    SET nationality_code = pCode,
        nationality_name = pName,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

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
