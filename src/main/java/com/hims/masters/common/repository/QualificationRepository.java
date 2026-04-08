package com.hims.masters.common.repository;

import com.hims.masters.common.entity.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface QualificationRepository extends JpaRepository<Qualification, Long> {
    @Query(value = "select * from fn_mt_qualification_dropdown()", nativeQuery = true)
    List<Map<String, Object>> qualificationDropDown();

    @Query(value = "select * from fn_mt_qualification_update(?1,?2,?3,?4)", nativeQuery = true)
    void qualificationUpdate(Long id, String code, String name, Boolean active);

    @Query(value = "select * from fn_mt_qualification_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> qualificationGetById(Long id);

    @Query(value = "select * from fn_mt_qualification_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> qualificationAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_qualification_listing_count(?1)", nativeQuery = true)
    Long qualificationListingCount(String searchString);

    @Query(value = "select * from fn_mt_qualification_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> qualificationListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_qualification_delete(?1)", nativeQuery = true)
    void qualificationDelete(Long id);
}
