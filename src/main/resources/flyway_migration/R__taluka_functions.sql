-- repeatable_checksum_marker: 20260313_01
CREATE OR REPLACE FUNCTION public.fn_mt_taluka_dropdown()
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT t.id, t.id, t.taluka_name::text
    FROM mt_taluka t
    WHERE t.delete_flag = false
      AND t.active = true
    ORDER BY t.taluka_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_taluka_get_by_id(
    ptalukaid bigint)
    RETURNS TABLE(
        id bigint,
        talukacode character varying,
        talukaname character varying,
        districtid bigint,
        districtname character varying,
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
        t.id,
        t.taluka_code,
        t.taluka_name,
        t.district_id,
        d.district_name,
        t.active,
        t.delete_flag,
        to_char(t.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(t.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_taluka t
    LEFT JOIN mt_district d ON d.id = t.district_id
    WHERE t.id = pTalukaId
      AND t.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_taluka_autocomplete(
    searchtext text)
    RETURNS TABLE(id bigint, label character varying, value character varying)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        t.id,
        t.taluka_name,
        t.taluka_code
    FROM mt_taluka t
    WHERE t.delete_flag = false
      AND t.active = true
      AND (
            t.taluka_code ILIKE '%' || searchText || '%'
         OR t.taluka_name ILIKE '%' || searchText || '%'
      )
    ORDER BY t.taluka_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_taluka_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        talukacode character varying,
        talukaname character varying,
        districtid bigint,
        districtname character varying,
        active boolean,
        createddate text
    )
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        t.id,
        t.taluka_code,
        t.taluka_name,
        t.district_id,
        d.district_name,
        t.active,
        to_char(t.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_taluka t
    LEFT JOIN mt_district d ON d.id = t.district_id
    WHERE t.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(t.taluka_code) LIKE LOWER(searchText) || '%'
         OR LOWER(t.taluka_name) LIKE LOWER(searchText) || '%'
         OR LOWER(d.district_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY t.created_date DESC, t.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_taluka_listing_count(
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT count(*)
    FROM mt_taluka t
    LEFT JOIN mt_district d ON d.id = t.district_id
    WHERE t.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(t.taluka_code) LIKE LOWER(searchText) || '%'
         OR LOWER(t.taluka_name) LIKE LOWER(searchText) || '%'
         OR LOWER(d.district_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_taluka_update(
    pid bigint,
    pcode character varying,
    pname character varying,
    pdistrictid bigint,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_taluka
    SET taluka_code = pCode,
        taluka_name = pName,
        district_id = pDistrictId,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_taluka_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_taluka
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
