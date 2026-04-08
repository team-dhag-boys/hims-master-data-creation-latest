CREATE OR REPLACE FUNCTION public.fn_mt_employee_category_dropdown(
)
    RETURNS TABLE(id bigint, value bigint, label text, employeetypeid bigint)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        ec.id,
        ec.id,
        ec.category::text,
        ec.employee_type_id
    FROM mt_employee_category ec
    WHERE ec.delete_flag = false
      AND ec.active = true
    ORDER BY ec.category;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_employee_category_get_by_id(
    pemployeecategoryid bigint)
    RETURNS TABLE(
        id bigint,
        category character varying,
        code character varying,
        employeetypeid bigint,
        employeetype character varying,
        active boolean,
        deleteflag boolean,
        createddate text,
        lastmodifieddate text
    )
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        ec.id,
        ec.category,
        ec.code,
        et.id,
        et.employee_type,
        ec.active,
        ec.delete_flag,
        to_char(ec.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(ec.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_employee_category ec
    LEFT JOIN mt_employee_type et
           ON et.id = ec.employee_type_id
    WHERE ec.id = pEmployeeCategoryId
      AND ec.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_employee_category_autocomplete(
    searchtext text)
    RETURNS TABLE(id bigint, label character varying, value character varying)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        ec.id,
        ec.category,
        ec.code
    FROM mt_employee_category ec
    WHERE ec.delete_flag = false
      AND ec.active = true
      AND (
            ec.category ILIKE '%' || searchText || '%'
         OR ec.code ILIKE '%' || searchText || '%'
      )
    ORDER BY ec.category;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_employee_category_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        category character varying,
        code character varying,
        employeetypeid bigint,
        employeetype character varying,
        active boolean,
        createddate text
    )
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        ec.id,
        ec.category,
        ec.code,
        et.id,
        et.employee_type,
        ec.active,
        to_char(ec.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_employee_category ec
    LEFT JOIN mt_employee_type et
           ON et.id = ec.employee_type_id
    WHERE ec.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(ec.category) LIKE LOWER(searchText) || '%'
         OR LOWER(ec.code) LIKE LOWER(searchText) || '%'
         OR LOWER(COALESCE(et.employee_type, '')) LIKE LOWER(searchText) || '%'
      )
    ORDER BY ec.created_date DESC, ec.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_employee_category_listing_count(
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT count(*)
    FROM mt_employee_category ec
    LEFT JOIN mt_employee_type et
           ON et.id = ec.employee_type_id
    WHERE ec.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(ec.category) LIKE LOWER(searchText) || '%'
         OR LOWER(ec.code) LIKE LOWER(searchText) || '%'
         OR LOWER(COALESCE(et.employee_type, '')) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_employee_category_update(
    pid bigint,
    pcategory character varying,
    pcode character varying,
    pemployeetypeid bigint,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
    UPDATE mt_employee_category
    SET category = pCategory,
        code = pCode,
        employee_type_id = pEmployeeTypeId,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_employee_category_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
    UPDATE mt_employee_category
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
