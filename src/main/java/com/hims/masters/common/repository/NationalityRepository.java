package com.hims.masters.common.repository;

import com.hims.masters.common.entity.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface NationalityRepository extends JpaRepository<Nationality, Long> {
    @Query(value = "select * from fn_mt_nationality_dropdown()", nativeQuery = true)
    List<Map<String, Object>> nationalityDropDown();

    @Query(value = "select * from fn_mt_nationality_update(?1,?2,?3,?4)", nativeQuery = true)
    void nationalityUpdate(Long id, String code, String name, Boolean active);

    @Query(value = "select * from fn_mt_nationality_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> nationalityGetById(Long id);

    @Query(value = "select * from fn_mt_nationality_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> nationalityAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_nationality_listing_count(?1)", nativeQuery = true)
    Long nationalityListingCount(String searchString);

    @Query(value = "select * from fn_mt_nationality_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> nationalityListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_nationality_delete(?1)", nativeQuery = true)
    void nationalityDelete(Long id);
}
