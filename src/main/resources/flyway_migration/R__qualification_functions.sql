-- repeatable_checksum_marker: 20260313_01
CREATE OR REPLACE FUNCTION public.fn_mt_qualification_dropdown()
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT q.id, q.id, q.qualification_name::text
    FROM mt_qualification q
    WHERE q.delete_flag = false
      AND q.active = true
    ORDER BY q.qualification_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_qualification_get_by_id(
    pqualificationid bigint)
    RETURNS TABLE(
        id bigint,
        qualificationcode character varying,
        qualificationname character varying,
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
        q.id,
        q.qualification_code,
        q.qualification_name,
        q.active,
        q.delete_flag,
        to_char(q.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(q.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_qualification q
    WHERE q.id = pQualificationId
      AND q.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_qualification_autocomplete(
    searchtext text)
    RETURNS TABLE(id bigint, label character varying, value character varying)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        q.id,
        q.qualification_name,
        q.qualification_code
    FROM mt_qualification q
    WHERE q.delete_flag = false
      AND q.active = true
      AND (
            q.qualification_code ILIKE '%' || searchText || '%'
         OR q.qualification_name ILIKE '%' || searchText || '%'
      )
    ORDER BY q.qualification_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_qualification_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        qualificationcode character varying,
        qualificationname character varying,
        active boolean,
        createddate text
    )
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        q.id,
        q.qualification_code,
        q.qualification_name,
        q.active,
        to_char(q.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_qualification q
    WHERE q.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(q.qualification_code) LIKE LOWER(searchText) || '%'
         OR LOWER(q.qualification_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY q.created_date DESC, q.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_qualification_listing_count(
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT count(*)
    FROM mt_qualification q
    WHERE q.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(q.qualification_code) LIKE LOWER(searchText) || '%'
         OR LOWER(q.qualification_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_qualification_update(
    pid bigint,
    pcode character varying,
    pname character varying,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_qualification
    SET qualification_code = pCode,
        qualification_name = pName,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_qualification_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_qualification
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
