CREATE OR REPLACE FUNCTION public.fn_mt_area_dropdown()
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT a.id, a.id, a.area_name::text
    FROM mt_area a
    WHERE a.delete_flag = false
      AND a.active = true
    ORDER BY a.area_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_area_get_by_id(
    pareaid bigint)
    RETURNS TABLE(
        id bigint,
        areacode character varying,
        areaname character varying,
        pincodeid bigint,
        pincode character varying,
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
        a.id,
        a.area_code,
        a.area_name,
        a.pincode_id,
        p.pincode,
        a.active,
        a.delete_flag,
        to_char(a.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(a.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_area a
    LEFT JOIN mt_pincode p ON p.id = a.pincode_id
    WHERE a.id = pAreaId
      AND a.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_area_autocomplete(
    searchtext text)
    RETURNS TABLE(id bigint, label character varying, value character varying)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        a.id,
        a.area_name,
        a.area_code
    FROM mt_area a
    WHERE a.delete_flag = false
      AND a.active = true
      AND (
            a.area_code ILIKE '%' || searchText || '%'
         OR a.area_name ILIKE '%' || searchText || '%'
      )
    ORDER BY a.area_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_area_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        areacode character varying,
        areaname character varying,
        pincodeid bigint,
        pincode character varying,
        active boolean,
        createddate text
    )
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        a.id,
        a.area_code,
        a.area_name,
        a.pincode_id,
        p.pincode,
        a.active,
        to_char(a.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_area a
    LEFT JOIN mt_pincode p ON p.id = a.pincode_id
    WHERE a.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(a.area_code) LIKE LOWER(searchText) || '%'
         OR LOWER(a.area_name) LIKE LOWER(searchText) || '%'
         OR LOWER(p.pincode) LIKE LOWER(searchText) || '%'
      )
    ORDER BY a.created_date DESC, a.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_area_listing_count(
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT count(*)
    FROM mt_area a
    LEFT JOIN mt_pincode p ON p.id = a.pincode_id
    WHERE a.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(a.area_code) LIKE LOWER(searchText) || '%'
         OR LOWER(a.area_name) LIKE LOWER(searchText) || '%'
         OR LOWER(p.pincode) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_area_update(
    pid bigint,
    pcode character varying,
    pname character varying,
    ppincodeid bigint,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_area
    SET area_code = pCode,
        area_name = pName,
        pincode_id = pPincodeId,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_area_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_area
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
