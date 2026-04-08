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
