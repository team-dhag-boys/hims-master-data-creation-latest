package com.hims.masters.common.repository;

import com.hims.masters.common.entity.Taluka;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface TalukaRepository extends JpaRepository<Taluka, Long> {
    @Query(value = "select * from fn_mt_taluka_dropdown()", nativeQuery = true)
    List<Map<String, Object>> talukaDropDown();

    @Query(value = "select * from fn_mt_taluka_update(?1,?2,?3,?4,?5)", nativeQuery = true)
    void talukaUpdate(Long id, String code, String name, Long districtId, Boolean active);

    @Query(value = "select * from fn_mt_taluka_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> talukaGetById(Long id);

    @Query(value = "select * from fn_mt_taluka_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> talukaAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_taluka_listing_count(?1)", nativeQuery = true)
    Long talukaListingCount(String searchString);

    @Query(value = "select * from fn_mt_taluka_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> talukaListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_taluka_delete(?1)", nativeQuery = true)
    void talukaDelete(Long id);
}
