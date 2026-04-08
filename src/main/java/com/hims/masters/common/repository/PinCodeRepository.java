package com.hims.masters.common.repository;

import com.hims.masters.common.entity.PinCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface PinCodeRepository extends JpaRepository<PinCode, Long> {
    @Query(value = "select * from fn_mt_pincode_dropdown()", nativeQuery = true)
    List<Map<String, Object>> pinCodeDropDown();

    @Query(value = "select * from fn_mt_pincode_update(?1,?2,?3,?4)", nativeQuery = true)
    void pinCodeUpdate(Long id, String pincode, Long cityId, Boolean active);

    @Query(value = "select * from fn_mt_pincode_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> pinCodeGetById(Long id);

    @Query(value = "select * from fn_mt_pincode_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> pinCodeAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_pincode_listing_count(?1)", nativeQuery = true)
    Long pinCodeListingCount(String searchString);

    @Query(value = "select * from fn_mt_pincode_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> pinCodeListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_pincode_delete(?1)", nativeQuery = true)
    void pinCodeDelete(Long id);
}
