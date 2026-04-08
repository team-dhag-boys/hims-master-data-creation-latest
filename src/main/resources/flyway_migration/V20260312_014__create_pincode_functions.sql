CREATE OR REPLACE FUNCTION public.fn_mt_pincode_dropdown()
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT p.id, p.id, p.pincode::text
    FROM mt_pincode p
    WHERE p.delete_flag = false
      AND p.active = true
    ORDER BY p.pincode;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_pincode_get_by_id(
    ppincodeid bigint)
    RETURNS TABLE(
        id bigint,
        pincode character varying,
        cityid bigint,
        cityname character varying,
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
        p.id,
        p.pincode,
        p.city_id,
        c.city_name,
        p.active,
        p.delete_flag,
        to_char(p.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(p.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_pincode p
    LEFT JOIN mt_city c ON c.id = p.city_id
    WHERE p.id = pPincodeId
      AND p.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_pincode_autocomplete(
    searchtext text)
    RETURNS TABLE(id bigint, label character varying, value character varying)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        p.id,
        p.pincode,
        p.pincode
    FROM mt_pincode p
    WHERE p.delete_flag = false
      AND p.active = true
      AND p.pincode ILIKE '%' || searchText || '%'
    ORDER BY p.pincode;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_pincode_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        pincode character varying,
        cityid bigint,
        cityname character varying,
        active boolean,
        createddate text
    )
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        p.id,
        p.pincode,
        p.city_id,
        c.city_name,
        p.active,
        to_char(p.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_pincode p
    LEFT JOIN mt_city c ON c.id = p.city_id
    WHERE p.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(p.pincode) LIKE LOWER(searchText) || '%'
         OR LOWER(c.city_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY p.created_date DESC, p.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_pincode_listing_count(
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT count(*)
    FROM mt_pincode p
    LEFT JOIN mt_city c ON c.id = p.city_id
    WHERE p.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(p.pincode) LIKE LOWER(searchText) || '%'
         OR LOWER(c.city_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_pincode_update(
    pid bigint,
    ppincode character varying,
    pcityid bigint,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_pincode
    SET pincode = pPincode,
        city_id = pCityId,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_pincode_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_pincode
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
