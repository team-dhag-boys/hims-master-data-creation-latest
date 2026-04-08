package com.hims.masters.common.repository;

import com.hims.masters.common.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface StateRepository extends JpaRepository<State, Long> {
    @Query(value = "select * from fn_mt_state_dropdown()", nativeQuery = true)
    List<Map<String, Object>> stateDropDown();

    @Query(value = "select * from fn_mt_state_update(?1,?2,?3,?4,?5)", nativeQuery = true)
    void stateUpdate(Long id, String code, String name, Long countryId, Boolean active);

    @Query(value = "select * from fn_mt_state_get_by_id(?1)", nativeQuery = true)
    Map<String, Object> stateGetById(Long id);

    @Query(value = "select * from fn_mt_state_autocomplete(?1)", nativeQuery = true)
    List<Map<String, Object>> stateAutocomplete(String searchString);

    @Query(value = "select * from fn_mt_state_listing_count(?1)", nativeQuery = true)
    Long stateListingCount(String searchString);

    @Query(value = "select * from fn_mt_state_listing(?1,?2,?3)", nativeQuery = true)
    List<Map<String, Object>> stateListing(Integer page, Integer size, String searchString);

    @Query(value = "select * from fn_mt_state_delete(?1)", nativeQuery = true)
    void stateDelete(Long id);
}
