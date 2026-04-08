-- repeatable_checksum_marker: 20260313_01
CREATE OR REPLACE FUNCTION public.fn_mt_bank_dropdown()
    RETURNS TABLE(id bigint, value bigint, label text)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT b.id, b.id, b.bank_name::text
    FROM mt_bank b
    WHERE b.delete_flag = false
      AND b.active = true
    ORDER BY b.bank_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_bank_get_by_id(
    pbankid bigint)
    RETURNS TABLE(
        id bigint,
        bankcode character varying,
        bankname character varying,
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
        b.id,
        b.bank_code,
        b.bank_name,
        b.active,
        b.delete_flag,
        to_char(b.created_date, 'DD-MM-YYYY HH24:MI:SS'),
        to_char(b.last_modified_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_bank b
    WHERE b.id = pBankId
      AND b.delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_bank_autocomplete(
    searchtext text)
    RETURNS TABLE(id bigint, label character varying, value character varying)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        b.id,
        b.bank_name,
        b.bank_code
    FROM mt_bank b
    WHERE b.delete_flag = false
      AND b.active = true
      AND (
            b.bank_code ILIKE '%' || searchText || '%'
         OR b.bank_name ILIKE '%' || searchText || '%'
      )
    ORDER BY b.bank_name;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_bank_listing(
    page integer,
    size integer,
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(
        id bigint,
        bankcode character varying,
        bankname character varying,
        active boolean,
        createddate text
    )
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT
        b.id,
        b.bank_code,
        b.bank_name,
        b.active,
        to_char(b.created_date, 'DD-MM-YYYY HH24:MI:SS')
    FROM mt_bank b
    WHERE b.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(b.bank_code) LIKE LOWER(searchText) || '%'
         OR LOWER(b.bank_name) LIKE LOWER(searchText) || '%'
      )
    ORDER BY b.created_date DESC, b.id DESC
    LIMIT size
    OFFSET page * size;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_bank_listing_count(
    searchtext character varying DEFAULT NULL::character varying)
    RETURNS TABLE(count bigint)
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT count(*)
    FROM mt_bank b
    WHERE b.delete_flag = false
      AND (
            searchText IS NULL
         OR LOWER(b.bank_code) LIKE LOWER(searchText) || '%'
         OR LOWER(b.bank_name) LIKE LOWER(searchText) || '%'
      );
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_bank_update(
    pid bigint,
    pcode character varying,
    pname character varying,
    pactive boolean)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_bank
    SET bank_code = pCode,
        bank_name = pName,
        active = pActive,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.fn_mt_bank_delete(
    pid bigint)
    RETURNS void
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    UPDATE mt_bank
    SET delete_flag = true,
        active = false,
        last_modified_date = now()
    WHERE id = pId
      AND delete_flag = false;
END;
$BODY$;
