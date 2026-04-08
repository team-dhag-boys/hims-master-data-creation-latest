CREATE OR REPLACE FUNCTION public.fn_mt_organization_dropdown()
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT o.id, o.id, o.organization_name::text
    FROM mt_organization o
    WHERE o.delete_flag = false
      AND o.active = true
    ORDER BY o.organization_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_organization_get_by_id(
    porganizationid bigint)
    RETURNS TABLE(
        id bigint,
        organizationcode character varying,
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
        o.id,
        o.organization_code,
        o.organization_name,
        o.active,
        o.delete_flag,
        to_char(o.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(o.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_organization o
    WHERE o.id = pOrganizationId
      AND o.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_organization_autocomplete(
    searchtext text)
    RETURNS TABLE(id bigint, label character varying, value character varying)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        o.id,
        o.organization_name,
        o.organization_code
    FROM mt_organization o
    WHERE o.delete_flag = false
      AND o.active = true
      AND (
            o.organization_code ILIKE '%' || searchText || '%'
         OR o.organization_name ILIKE '%' || searchText || '%'
      )
    ORDER BY o.organization_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_organization_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        organizationcode character varying,
        organizationname character varying,
        active boolean,
        createddate text
    )
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        o.id,
        o.organization_code,
        o.organization_name,
        o.active,
        to_char(o.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_organization o
    WHERE o.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(o.organization_code) LIKE LOWER(searchText) || '%'
         OR LOWER(o.organization_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY o.created_date DESC, o.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_organization_listing_count(
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT count(*)
    FROM mt_organization o
    WHERE o.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(o.organization_code) LIKE LOWER(searchText) || '%'
         OR LOWER(o.organization_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_organization_update(
    pid bigint,
    pcode character varying,
    pname character varying,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_organization
    SET organization_code = pCode,
        organization_name = pName,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_organization_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_organization
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
