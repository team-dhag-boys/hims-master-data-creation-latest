package com.hims.masters.common.repository;

import com.hims.masters.common.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface AreaRepository extends JpaRepository<Area, Long> {
    @Query(value = "select * from fn_mt_area_dropdown()", nativeQuery = true)
    List<Map<String, Object>> areaDropDown();

    @Query(value = "select * from fn_mt_area_update(?1,?2,?3,?4,?5)", nativeQuery = true)
    void areaUpdate(Long id, String code, String name, Long pinCodeId, Boolean active);

    @Query(value = "select * from fn_mt_area_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> areaGetById(Long id);

    @Query(value = "select * from fn_mt_area_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> areaAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_area_listing_count(?1)", nativeQuery = true)
    Long areaListingCount(String searchString);

    @Query(value = "select * from fn_mt_area_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> areaListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_area_delete(?1)", nativeQuery = true)
    void areaDelete(Long id);
}
