CREATE OR REPLACE FUNCTION public.fn_mt_district_dropdown()
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT d.id, d.id, d.district_name::text
    FROM mt_district d
    WHERE d.delete_flag = false
      AND d.active = true
    ORDER BY d.district_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_district_get_by_id(
    pdistrictid bigint)
    RETURNS TABLE(
        id bigint,
        districtcode character varying,
        districtname character varying,
        stateid bigint,
        statename character varying,
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
        d.district_code,
        d.district_name,
        d.state_id,
        s.state_name,
        d.active,
        d.delete_flag,
        to_char(d.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(d.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_district d
    LEFT JOIN mt_state s ON s.id = d.state_id
    WHERE d.id = pDistrictId
      AND d.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_district_autocomplete(
    searchtext text)
    RETURNS TABLE(id bigint, label character varying, value character varying)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        d.id,
        d.district_name,
        d.district_code
    FROM mt_district d
    WHERE d.delete_flag = false
      AND d.active = true
      AND (
            d.district_code ILIKE '%' || searchText || '%'
         OR d.district_name ILIKE '%' || searchText || '%'
      )
    ORDER BY d.district_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_district_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        districtcode character varying,
        districtname character varying,
        stateid bigint,
        statename character varying,
        active boolean,
        createddate text
    )
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        d.id,
        d.district_code,
        d.district_name,
        d.state_id,
        s.state_name,
        d.active,
        to_char(d.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_district d
    LEFT JOIN mt_state s ON s.id = d.state_id
    WHERE d.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(d.district_code) LIKE LOWER(searchText) || '%'
         OR LOWER(d.district_name) LIKE LOWER(searchText) || '%'
         OR LOWER(s.state_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY d.created_date DESC, d.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_district_listing_count(
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT count(*)
    FROM mt_district d
    LEFT JOIN mt_state s ON s.id = d.state_id
    WHERE d.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(d.district_code) LIKE LOWER(searchText) || '%'
         OR LOWER(d.district_name) LIKE LOWER(searchText) || '%'
         OR LOWER(s.state_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_district_update(
    pid bigint,
    pcode character varying,
    pname character varying,
    pstateid bigint,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_district
    SET district_code = pCode,
        district_name = pName,
        state_id = pStateId,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_district_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_district
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
