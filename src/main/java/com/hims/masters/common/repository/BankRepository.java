package com.hims.masters.common.repository;

import com.hims.masters.common.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface BankRepository extends JpaRepository<Bank, Long> {
    @Query(value = "select * from fn_mt_bank_dropdown()", nativeQuery = true)
    List<Map<String, Object>> bankDropDown();

    @Query(value = "select * from fn_mt_bank_update(?1,?2,?3,?4)", nativeQuery = true)
    void bankUpdate(Long id, String code, String name, Boolean active);

    @Query(value = "select * from fn_mt_bank_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> bankGetById(Long id);

    @Query(value = "select * from fn_mt_bank_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> bankAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_bank_listing_count(?1)", nativeQuery = true)
    Long bankListingCount(String searchString);

    @Query(value = "select * from fn_mt_bank_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> bankListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_bank_delete(?1)", nativeQuery = true)
    void bankDelete(Long id);
}
