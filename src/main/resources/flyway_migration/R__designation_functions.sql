-- repeatable_checksum_marker: 20260313_01
CREATE OR REPLACE FUNCTION public.fn_mt_designation_dropdown()
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT d.id, d.id, d.designation::text
    FROM mt_designation d
    WHERE d.delete_flag = false
      AND d.active = true
    ORDER BY d.designation;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_designation_get_by_id(
    pdesignationid bigint)
    RETURNS TABLE(
        id bigint,
        designation character varying,
        code character varying,
        active boolean,
        deleteflag boolean,
        createddate text,
        lastmodifieddate text
    )
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        d.id,
        d.designation,
        d.code,
        d.active,
        d.delete_flag,
        to_char(d.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(d.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_designation d
    WHERE d.id = pDesignationId
      AND d.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_designation_autocomplete(
    searchtext text)
    RETURNS TABLE(id bigint, label character varying, value character varying)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        d.id,
        d.designation,
        d.code
    FROM mt_designation d
    WHERE d.delete_flag = false
      AND d.active = true
      AND (
            d.designation ILIKE '%' || searchText || '%'
         OR d.code ILIKE '%' || searchText || '%'
      )
    ORDER BY d.designation;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_designation_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        designation character varying,
        code character varying,
        active boolean,
        createddate text
    )
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        d.id,
        d.designation,
        d.code,
        d.active,
        to_char(d.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_designation d
    WHERE d.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(d.designation) LIKE LOWER(searchText) || '%'
         OR LOWER(d.code) LIKE LOWER(searchText) || '%'
      )
    ORDER BY d.created_date DESC, d.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_designation_listing_count(
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT count(*)
    FROM mt_designation d
    WHERE d.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(d.designation) LIKE LOWER(searchText) || '%'
         OR LOWER(d.code) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_designation_update(
    pid bigint,
    pdesignation character varying,
    pcode character varying,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_designation
    SET designation = pDesignation,
        code = pCode,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_designation_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_designation
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
