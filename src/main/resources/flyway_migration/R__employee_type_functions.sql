-- repeatable_checksum_marker: 20260313_01
CREATE OR REPLACE FUNCTION public.fn_mt_employee_type_dropdown(
)
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        et.id,
        et.id,
        et.employee_type::text
    FROM mt_employee_type et
    WHERE et.delete_flag = false
      AND et.active = true
    ORDER BY et.employee_type;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_employee_type_get_by_id(
    pemployeetypeid bigint)
    RETURNS TABLE(
        id bigint,
        employeetype character varying,
        isclinical boolean,
        applicabletodoctortype boolean,
        isdoctor boolean,
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
        et.id,
        et.employee_type,
        et.is_clinical,
        et.applicable_to_doctor_type,
        et.is_doctor,
        et.active,
        et.delete_flag,
        to_char(et.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(et.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_employee_type et
    WHERE et.id = pEmployeeTypeId
      AND et.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_employee_type_autocomplete(
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
        et.id,
        et.employee_type,
        et.employee_type
    FROM mt_employee_type et
    WHERE et.delete_flag = false
      AND et.active = true
      AND et.employee_type ILIKE '%' || searchText || '%'
    ORDER BY et.employee_type;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_employee_type_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        employeetype character varying,
        isclinical boolean,
        applicabletodoctortype boolean,
        isdoctor boolean,
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
        et.id,
        et.employee_type,
        et.is_clinical,
        et.applicable_to_doctor_type,
        et.is_doctor,
        et.active,
        to_char(et.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_employee_type et
    WHERE et.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(et.employee_type) LIKE LOWER(searchText) || '%'
      )
    ORDER BY et.created_date DESC, et.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_employee_type_listing_count(
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
    FROM mt_employee_type et
    WHERE et.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(et.employee_type) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_employee_type_update(
    pid bigint,
    pemployeetype character varying,
    pisclinical boolean,
    papplicabletodoctortype boolean,
    pisdoctor boolean,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
    UPDATE mt_employee_type
    SET employee_type = pEmployeeType,
        is_clinical = pIsClinical,
        applicable_to_doctor_type = pApplicableToDoctorType,
        is_doctor = pIsDoctor,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_employee_type_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
    UPDATE mt_employee_type
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
