CREATE OR REPLACE FUNCTION public.fn_mt_country_dropdown(
)
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT c.id, c.id, c.country_name::text
    FROM mt_country c
    WHERE c.delete_flag = false
      AND c.active = true
    ORDER BY c.country_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_country_get_by_id(
    pcountryid bigint)
    RETURNS TABLE(
        id bigint,
        countrycode character varying,
        countryname character varying,
        isdcode character varying,
        mobilelength character varying,
        isdefault boolean,
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
        c.country_code,
        c.country_name,
        c.isd_code,
        c.mobile_length,
        c.is_default,
        c.active,
        c.delete_flag,
        to_char(c.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(c.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_country c
    WHERE c.id = pCountryId
      AND c.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_country_autocomplete(
    searchtext text)
    RETURNS TABLE(id bigint, label character varying, value character varying)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        c.id,
        c.country_name,
        c.country_code
    FROM mt_country c
    WHERE c.delete_flag = false
      AND c.active = true
      AND (
            c.country_code ILIKE '%' || searchText || '%'
         OR c.country_name ILIKE '%' || searchText || '%'
         OR c.isd_code ILIKE '%' || searchText || '%'
      )
    ORDER BY c.country_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_country_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        countrycode character varying,
        countryname character varying,
        isdcode character varying,
        mobilelength character varying,
        active boolean,
        createddate text
    )
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        c.id,
        c.country_code,
        c.country_name,
        c.isd_code,
        c.mobile_length,
        c.active,
        to_char(c.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_country c
    WHERE c.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(c.country_code) LIKE LOWER(searchText) || '%'
         OR LOWER(c.country_name) LIKE LOWER(searchText) || '%'
         OR LOWER(c.isd_code) LIKE LOWER(searchText) || '%'
      )
    ORDER BY c.created_date DESC, c.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_country_listing_count(
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT count(*)
    FROM mt_country c
    WHERE c.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(c.country_code) LIKE LOWER(searchText) || '%'
         OR LOWER(c.country_name) LIKE LOWER(searchText) || '%'
         OR LOWER(c.isd_code) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_country_update(
    pid bigint,
    pcode character varying,
    pname character varying,
    pisdcode character varying,
    pmobilelength character varying,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_country
    SET country_code = pCode,
        country_name = pName,
        isd_code = pIsdCode,
        mobile_length = pMobileLength,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_country_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_country
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
