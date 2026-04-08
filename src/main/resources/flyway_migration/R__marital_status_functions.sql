-- repeatable_checksum_marker: 20260313_01
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

CREATE OR REPLACE FUNCTION public.fn_mt_marital_status_get_by_id(
    pmaritalstatusid bigint)
    RETURNS TABLE(
        id bigint,
        maritalstatuscode character varying,
        maritalstatusname character varying,
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
        ms.id,
        ms.marital_status_code,
        ms.marital_status_name,
        ms.active,
        ms.delete_flag,
        to_char(ms.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(ms.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_marital_status ms
    WHERE ms.id = pMaritalStatusId
      AND ms.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_marital_status_autocomplete(
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
        ms.id,
        ms.marital_status_name,
        ms.marital_status_code
    FROM mt_marital_status ms
    WHERE ms.delete_flag = false
      AND ms.active = true
      AND (
            ms.marital_status_code ILIKE '%' || searchText || '%'
         OR ms.marital_status_name ILIKE '%' || searchText || '%'
      )
    ORDER BY ms.marital_status_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_marital_status_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        maritalstatuscode character varying,
        maritalstatusname character varying,
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
        ms.id,
        ms.marital_status_code,
        ms.marital_status_name,
        ms.active,
        to_char(ms.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_marital_status ms
    WHERE ms.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(ms.marital_status_code) LIKE LOWER(searchText) || '%'
         OR LOWER(ms.marital_status_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY ms.created_date DESC, ms.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_marital_status_listing_count(
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
    FROM mt_marital_status ms
    WHERE ms.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(ms.marital_status_code) LIKE LOWER(searchText) || '%'
         OR LOWER(ms.marital_status_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_marital_status_update(
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
    UPDATE mt_marital_status
    SET marital_status_code = pCode,
        marital_status_name = pName,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_marital_status_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
    UPDATE mt_marital_status
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
