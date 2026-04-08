-- repeatable_checksum_marker: 20260313_01
CREATE OR REPLACE FUNCTION public.fn_mt_department_unit_mapping_dropdown()
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT m.id, m.id, (d.department_name || ' - ' || u.unit_name)::text
    FROM mt_department_unit_mapping m
    LEFT JOIN mt_department d ON d.id = m.department_id
    LEFT JOIN mt_unit u ON u.id = m.unit_id
    WHERE m.delete_flag = false
      AND m.active = true
    ORDER BY d.department_name, u.unit_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_department_unit_mapping_get_by_id(
    pmappingid bigint)
    RETURNS TABLE(
        id bigint,
        departmentid bigint,
        departmentname character varying,
        unitid bigint,
        unitname character varying,
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
        m.id,
        m.department_id,
        d.department_name,
        m.unit_id,
        u.unit_name,
        m.active,
        m.delete_flag,
        to_char(m.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(m.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_department_unit_mapping m
    LEFT JOIN mt_department d ON d.id = m.department_id
    LEFT JOIN mt_unit u ON u.id = m.unit_id
    WHERE m.id = pMappingId
      AND m.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_department_unit_mapping_autocomplete(
    searchtext text)
    RETURNS TABLE(id bigint, label text, value text)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        m.id,
        (d.department_name || ' - ' || u.unit_name)::text,
        (d.department_name || ' - ' || u.unit_name)::text
    FROM mt_department_unit_mapping m
    LEFT JOIN mt_department d ON d.id = m.department_id
    LEFT JOIN mt_unit u ON u.id = m.unit_id
    WHERE m.delete_flag = false
      AND m.active = true
      AND (
            d.department_name ILIKE '%' || searchText || '%'
         OR u.unit_name ILIKE '%' || searchText || '%'
      )
    ORDER BY d.department_name, u.unit_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_department_unit_mapping_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        departmentid bigint,
        departmentname character varying,
        unitid bigint,
        unitname character varying,
        active boolean,
        createddate text
    )
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        m.id,
        m.department_id,
        d.department_name,
        m.unit_id,
        u.unit_name,
        m.active,
        to_char(m.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_department_unit_mapping m
    LEFT JOIN mt_department d ON d.id = m.department_id
    LEFT JOIN mt_unit u ON u.id = m.unit_id
    WHERE m.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(d.department_name) LIKE LOWER(searchText) || '%'
         OR LOWER(u.unit_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY m.created_date DESC, m.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_department_unit_mapping_listing_count(
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT count(*)
    FROM mt_department_unit_mapping m
    LEFT JOIN mt_department d ON d.id = m.department_id
    LEFT JOIN mt_unit u ON u.id = m.unit_id
    WHERE m.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(d.department_name) LIKE LOWER(searchText) || '%'
         OR LOWER(u.unit_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_department_unit_mapping_update(
    pid bigint,
    pdepartmentid bigint,
    punitid bigint,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_department_unit_mapping
    SET department_id = pDepartmentId,
        unit_id = pUnitId,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_department_unit_mapping_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_department_unit_mapping
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
