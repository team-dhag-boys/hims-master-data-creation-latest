package com.hims.masters.common.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.common.dto.request.MaritalStatusListRequestDto;
import com.hims.masters.common.dto.request.MaritalStatusRequestDto;
import com.hims.masters.common.repository.MaritalStatusRepository;
import com.hims.masters.common.services.impl.MaritalStatusServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MaritalStatusServiceImplTest {
    private final MaritalStatusRepository repository = mock(MaritalStatusRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final MaritalStatusServiceImpl service = new MaritalStatusServiceImpl(repository, apiResponseFactory);

    @Test
    void saveMaritalStatus_happyPath() {
        MaritalStatusRequestDto dto = new MaritalStatusRequestDto();
        dto.setMaritalStatusCode("M");
        dto.setMaritalStatusName("Married");
        ResponseEntity<?> responseEntity = service.saveMaritalStatus(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateMaritalStatus_validationFailure_nullId() {
        MaritalStatusRequestDto dto = new MaritalStatusRequestDto();
        dto.setMaritalStatusCode("M");
        dto.setMaritalStatusName("Married");
        ResponseEntity<?> responseEntity = service.updateMaritalStatus(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Marital status id is null", response.getMessage());
    }

    @Test
    void maritalStatusDropDown_negative_emptyList() {
        when(repository.maritalStatusDropDown()).thenReturn(List.of());
        ResponseEntity<?> responseEntity = service.maritalStatusDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void maritalStatusList_validationFailure_negativePage() {
        MaritalStatusListRequestDto dto = new MaritalStatusListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);
        ResponseEntity<?> responseEntity = service.maritalStatusList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void deleteMaritalStatus_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteMaritalStatus(1L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).maritalStatusDelete(1L);
    }
}
