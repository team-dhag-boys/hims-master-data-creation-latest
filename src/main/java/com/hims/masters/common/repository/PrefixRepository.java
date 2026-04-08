package com.hims.masters.common.repository;

import com.hims.masters.common.entity.Prefix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;


public interface PrefixRepository extends JpaRepository<Prefix, Long> {

    @Query(value = "select * from fn_mt_prefix_dropdown()", nativeQuery = true)
    List<Map<String, Object>> getPrefixDropDown();

    @Query(value = "select * from fn_mt_prefix_update(?1,?2,?3,?4,?5)", nativeQuery = true)
    void prefixUpdate(Long id, String prefixCode, String prefixName, Boolean active, Long genderId);

    @Query(value = "select * from fn_mt_prefix_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> prefixGetById(Long id);

    @Query(value = "select * from fn_mt_prefix_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> prefixAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_prefix_listing_count(?1)", nativeQuery = true)
    Long prefixListingCount(String searchString);

    @Query(value = "select * from fn_mt_prefix_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> prefixListing(int page,int size,String searchString);

    @Query(value = "select * from fn_mt_prefix_delete(?1)", nativeQuery = true)
    void prefixDelete(Long id);

}
