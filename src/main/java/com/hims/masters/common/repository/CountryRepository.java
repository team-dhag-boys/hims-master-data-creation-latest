package com.hims.masters.common.repository;

import com.hims.masters.common.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query(value = "select * from fn_mt_country_dropdown()", nativeQuery = true)
    List<Map<String, Object>> countryDropDown();

    @Query(value = "select * from fn_mt_country_update(?1,?2,?3,?4,?5,?6)", nativeQuery = true)
    void countryUpdate(Long id, String code, String name, String isdCode, String mobileLength, Boolean active);

    @Query(value = "select * from fn_mt_country_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> countryGetById(Long id);

    @Query(value = "select * from fn_mt_country_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> countryAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_country_listing_count(?1)", nativeQuery = true)
    Long countryListingCount(String searchString);

    @Query(value = "select * from fn_mt_country_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> countryListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_country_delete(?1)", nativeQuery = true)
    void countryDelete(Long id);
}
