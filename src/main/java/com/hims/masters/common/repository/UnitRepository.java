package com.hims.masters.common.repository;

import com.hims.masters.common.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    @Query(value = "select * from fn_mt_unit_dropdown()", nativeQuery = true)
    List<Map<String, Object>> unitDropDown();

    @Query(value = "select * from fn_mt_unit_update(?1,?2,?3,?4,?5)", nativeQuery = true)
    void unitUpdate(Long id, String code, String name, Long organizationId, Boolean active);

    @Query(value = "select * from fn_mt_unit_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> unitGetById(Long id);

    @Query(value = "select * from fn_mt_unit_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> unitAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_unit_listing_count(?1)", nativeQuery = true)
    Long unitListingCount(String searchString);

    @Query(value = "select * from fn_mt_unit_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> unitListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_unit_delete(?1)", nativeQuery = true)
    void unitDelete(Long id);
}
