package com.hims.masters.common.repository;

import com.hims.masters.common.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CityRepository extends JpaRepository<City, Long> {
    @Query(value = "select * from fn_mt_city_dropdown()", nativeQuery = true)
    List<Map<String, Object>> cityDropDown();

    @Query(value = "select * from fn_mt_city_update(?1,?2,?3,?4,?5)", nativeQuery = true)
    void cityUpdate(Long id, String code, String name, Long talukaId, Boolean active);

    @Query(value = "select * from fn_mt_city_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> cityGetById(Long id);

    @Query(value = "select * from fn_mt_city_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> cityAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_city_listing_count(?1)", nativeQuery = true)
    Long cityListingCount(String searchString);

    @Query(value = "select * from fn_mt_city_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> cityListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_city_delete(?1)", nativeQuery = true)
    void cityDelete(Long id);
}
