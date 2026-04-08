-- repeatable_checksum_marker: 20260313_01
CREATE OR REPLACE FUNCTION public.fn_mt_city_dropdown()
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT c.id, c.id, c.city_name::text
    FROM mt_city c
    WHERE c.delete_flag = false
      AND c.active = true
    ORDER BY c.city_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_city_get_by_id(
    pcityid bigint)
    RETURNS TABLE(
        id bigint,
        citycode character varying,
        cityname character varying,
        talukaid bigint,
        talukaname character varying,
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
        c.id,
        c.city_code,
        c.city_name,
        c.taluka_id,
        t.taluka_name,
        c.active,
        c.delete_flag,
        to_char(c.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(c.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_city c
    LEFT JOIN mt_taluka t ON t.id = c.taluka_id
    WHERE c.id = pCityId
      AND c.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_city_autocomplete(
    searchtext text)
    RETURNS TABLE(id bigint, label character varying, value character varying)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        c.id,
        c.city_name,
        c.city_code
    FROM mt_city c
    WHERE c.delete_flag = false
      AND c.active = true
      AND (
            c.city_code ILIKE '%' || searchText || '%'
         OR c.city_name ILIKE '%' || searchText || '%'
      )
    ORDER BY c.city_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_city_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        citycode character varying,
        cityname character varying,
        talukaid bigint,
        talukaname character varying,
        active boolean,
        createddate text
    )
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        c.id,
        c.city_code,
        c.city_name,
        c.taluka_id,
        t.taluka_name,
        c.active,
        to_char(c.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_city c
    LEFT JOIN mt_taluka t ON t.id = c.taluka_id
    WHERE c.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(c.city_code) LIKE LOWER(searchText) || '%'
         OR LOWER(c.city_name) LIKE LOWER(searchText) || '%'
         OR LOWER(t.taluka_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY c.created_date DESC, c.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_city_listing_count(
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT count(*)
    FROM mt_city c
    LEFT JOIN mt_taluka t ON t.id = c.taluka_id
    WHERE c.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(c.city_code) LIKE LOWER(searchText) || '%'
         OR LOWER(c.city_name) LIKE LOWER(searchText) || '%'
         OR LOWER(t.taluka_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_city_update(
    pid bigint,
    pcode character varying,
    pname character varying,
    ptalukaid bigint,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_city
    SET city_code = pCode,
        city_name = pName,
        taluka_id = pTalukaId,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_city_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_city
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
