-- repeatable_checksum_marker: 20260313_01
CREATE OR REPLACE FUNCTION public.fn_mt_state_dropdown()
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT s.id, s.id, s.state_name::text
    FROM mt_state s
    WHERE s.delete_flag = false
      AND s.active = true
    ORDER BY s.state_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_state_get_by_id(
    pstateid bigint)
    RETURNS TABLE(
        id bigint,
        statecode character varying,
        statename character varying,
        countryid bigint,
        countryname character varying,
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
        s.id,
        s.state_code,
        s.state_name,
        s.country_id,
        c.country_name,
        s.active,
        s.delete_flag,
        to_char(s.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(s.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_state s
    LEFT JOIN mt_country c ON c.id = s.country_id
    WHERE s.id = pStateId
      AND s.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_state_autocomplete(
    searchtext text)
    RETURNS TABLE(id bigint, label character varying, value character varying)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        s.id,
        s.state_name,
        s.state_code
    FROM mt_state s
    WHERE s.delete_flag = false
      AND s.active = true
      AND (
            s.state_code ILIKE '%' || searchText || '%'
         OR s.state_name ILIKE '%' || searchText || '%'
      )
    ORDER BY s.state_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_state_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        statecode character varying,
        statename character varying,
        countryid bigint,
        countryname character varying,
        active boolean,
        createddate text
    )
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        s.id,
        s.state_code,
        s.state_name,
        s.country_id,
        c.country_name,
        s.active,
        to_char(s.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_state s
    LEFT JOIN mt_country c ON c.id = s.country_id
    WHERE s.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(s.state_code) LIKE LOWER(searchText) || '%'
         OR LOWER(s.state_name) LIKE LOWER(searchText) || '%'
         OR LOWER(c.country_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY s.created_date DESC, s.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_state_listing_count(
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT count(*)
    FROM mt_state s
    LEFT JOIN mt_country c ON c.id = s.country_id
    WHERE s.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(s.state_code) LIKE LOWER(searchText) || '%'
         OR LOWER(s.state_name) LIKE LOWER(searchText) || '%'
         OR LOWER(c.country_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_state_update(
    pid bigint,
    pcode character varying,
    pname character varying,
    pcountryid bigint,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_state
    SET state_code = pCode,
        state_name = pName,
        country_id = pCountryId,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_state_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_state
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
