package com.hims.masters.common.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.common.dto.request.BloodGroupListRequestDto;
import com.hims.masters.common.dto.request.BloodGroupRequestDto;
import com.hims.masters.common.repository.BloodGroupRepository;
import com.hims.masters.common.services.impl.BloodGroupServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BloodGroupServiceImplTest {
    private final BloodGroupRepository repository = mock(BloodGroupRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final BloodGroupServiceImpl service = new BloodGroupServiceImpl(repository, apiResponseFactory);

    @Test
    void saveBloodGroup_happyPath() {
        BloodGroupRequestDto dto = new BloodGroupRequestDto();
        dto.setBloodGroupCode("A+");
        dto.setBloodGroupName("A+");
        ResponseEntity<?> responseEntity = service.saveBloodGroup(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateBloodGroup_validationFailure_nullId() {
        BloodGroupRequestDto dto = new BloodGroupRequestDto();
        dto.setBloodGroupCode("A+");
        dto.setBloodGroupName("A+");
        ResponseEntity<?> responseEntity = service.updateBloodGroup(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Blood group id is null", response.getMessage());
    }

    @Test
    void bloodGroupDropDown_negative_emptyList() {
        when(repository.bloodGroupDropDown()).thenReturn(List.of());
        ResponseEntity<?> responseEntity = service.bloodGroupDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void bloodGroupList_validationFailure_negativePage() {
        BloodGroupListRequestDto dto = new BloodGroupListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);
        ResponseEntity<?> responseEntity = service.bloodGroupList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void deleteBloodGroup_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteBloodGroup(1L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).bloodGroupDelete(1L);
    }
}
