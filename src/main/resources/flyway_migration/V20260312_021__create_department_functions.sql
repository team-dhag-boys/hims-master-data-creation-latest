CREATE OR REPLACE FUNCTION public.fn_mt_department_dropdown()
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT d.id, d.id, d.department_name::text
    FROM mt_department d
    WHERE d.delete_flag = false
      AND d.active = true
    ORDER BY d.department_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_department_get_by_id(
    pdepartmentid bigint)
    RETURNS TABLE(
        id bigint,
        departmentcode character varying,
        departmentname character varying,
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
        d.department_code,
        d.department_name,
        d.active,
        d.delete_flag,
        to_char(d.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(d.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_department d
    WHERE d.id = pDepartmentId
      AND d.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_department_autocomplete(
    searchtext text)
    RETURNS TABLE(id bigint, label character varying, value character varying)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        d.id,
        d.department_name,
        d.department_code
    FROM mt_department d
    WHERE d.delete_flag = false
      AND d.active = true
      AND (
            d.department_code ILIKE '%' || searchText || '%'
         OR d.department_name ILIKE '%' || searchText || '%'
      )
    ORDER BY d.department_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_department_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        departmentcode character varying,
        departmentname character varying,
        active boolean,
        createddate text
    )
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        d.id,
        d.department_code,
        d.department_name,
        d.active,
        to_char(d.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_department d
    WHERE d.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(d.department_code) LIKE LOWER(searchText) || '%'
         OR LOWER(d.department_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY d.created_date DESC, d.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_department_listing_count(
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT count(*)
    FROM mt_department d
    WHERE d.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(d.department_code) LIKE LOWER(searchText) || '%'
         OR LOWER(d.department_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_department_update(
    pid bigint,
    pcode character varying,
    pname character varying,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_department
    SET department_code = pCode,
        department_name = pName,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_department_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_department
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
