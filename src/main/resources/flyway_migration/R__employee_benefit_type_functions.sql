-- repeatable_checksum_marker: 20260313_01
CREATE OR REPLACE FUNCTION public.fn_mt_employee_benefit_type_dropdown()
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT e.id, e.id, e.benefit_type_name::text
    FROM mt_employee_benefit_type e
    WHERE e.delete_flag = false
      AND e.active = true
    ORDER BY e.benefit_type_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_employee_benefit_type_get_by_id(
    pbenefittypeid bigint)
    RETURNS TABLE(
        id bigint,
        benefittypecode character varying,
        benefittypename character varying,
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
        e.id,
        e.benefit_type_code,
        e.benefit_type_name,
        e.active,
        e.delete_flag,
        to_char(e.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(e.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_employee_benefit_type e
    WHERE e.id = pBenefitTypeId
      AND e.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_employee_benefit_type_autocomplete(
    searchtext text)
    RETURNS TABLE(id bigint, label character varying, value character varying)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        e.id,
        e.benefit_type_name,
        e.benefit_type_code
    FROM mt_employee_benefit_type e
    WHERE e.delete_flag = false
      AND e.active = true
      AND (
            e.benefit_type_code ILIKE '%' || searchText || '%'
         OR e.benefit_type_name ILIKE '%' || searchText || '%'
      )
    ORDER BY e.benefit_type_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_employee_benefit_type_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        benefittypecode character varying,
        benefittypename character varying,
        active boolean,
        createddate text
    )
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        e.id,
        e.benefit_type_code,
        e.benefit_type_name,
        e.active,
        to_char(e.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_employee_benefit_type e
    WHERE e.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(e.benefit_type_code) LIKE LOWER(searchText) || '%'
         OR LOWER(e.benefit_type_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY e.created_date DESC, e.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_employee_benefit_type_listing_count(
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT count(*)
    FROM mt_employee_benefit_type e
    WHERE e.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(e.benefit_type_code) LIKE LOWER(searchText) || '%'
         OR LOWER(e.benefit_type_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_employee_benefit_type_update(
    pid bigint,
    pcode character varying,
    pname character varying,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_employee_benefit_type
    SET benefit_type_code = pCode,
        benefit_type_name = pName,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_employee_benefit_type_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_employee_benefit_type
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
