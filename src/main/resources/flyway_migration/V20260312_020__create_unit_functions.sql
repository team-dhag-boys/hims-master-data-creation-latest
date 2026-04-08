CREATE OR REPLACE FUNCTION public.fn_mt_unit_dropdown()
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT u.id, u.id, u.unit_name::text
    FROM mt_unit u
    WHERE u.delete_flag = false
      AND u.active = true
    ORDER BY u.unit_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_unit_get_by_id(
    punitid bigint)
    RETURNS TABLE(
        id bigint,
        unitcode character varying,
        unitname character varying,
        organizationid bigint,
        organizationname character varying,
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
        u.id,
        u.unit_code,
        u.unit_name,
        u.organization_id,
        o.organization_name,
        u.active,
        u.delete_flag,
        to_char(u.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(u.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_unit u
    LEFT JOIN mt_organization o ON o.id = u.organization_id
    WHERE u.id = pUnitId
      AND u.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_unit_autocomplete(
    searchtext text)
    RETURNS TABLE(id bigint, label character varying, value character varying)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        u.id,
        u.unit_name,
        u.unit_code
    FROM mt_unit u
    WHERE u.delete_flag = false
      AND u.active = true
      AND (
            u.unit_code ILIKE '%' || searchText || '%'
         OR u.unit_name ILIKE '%' || searchText || '%'
      )
    ORDER BY u.unit_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_unit_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        unitcode character varying,
        unitname character varying,
        organizationid bigint,
        organizationname character varying,
        active boolean,
        createddate text
    )
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        u.id,
        u.unit_code,
        u.unit_name,
        u.organization_id,
        o.organization_name,
        u.active,
        to_char(u.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_unit u
    LEFT JOIN mt_organization o ON o.id = u.organization_id
    WHERE u.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(u.unit_code) LIKE LOWER(searchText) || '%'
         OR LOWER(u.unit_name) LIKE LOWER(searchText) || '%'
         OR LOWER(o.organization_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY u.created_date DESC, u.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_unit_listing_count(
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT count(*)
    FROM mt_unit u
    LEFT JOIN mt_organization o ON o.id = u.organization_id
    WHERE u.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(u.unit_code) LIKE LOWER(searchText) || '%'
         OR LOWER(u.unit_name) LIKE LOWER(searchText) || '%'
         OR LOWER(o.organization_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_unit_update(
    pid bigint,
    pcode character varying,
    pname character varying,
    porganizationid bigint,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_unit
    SET unit_code = pCode,
        unit_name = pName,
        organization_id = pOrganizationId,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_unit_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_unit
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
