package com.hims.masters.common.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.common.dto.request.DepartmentUnitMappingListRequestDto;
import com.hims.masters.common.dto.request.DepartmentUnitMappingRequestDto;
import com.hims.masters.common.repository.DepartmentUnitMappingRepository;
import com.hims.masters.common.services.impl.DepartmentUnitMappingServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DepartmentUnitMappingServiceImplTest {
    private final DepartmentUnitMappingRepository repository = mock(DepartmentUnitMappingRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final DepartmentUnitMappingServiceImpl service = new DepartmentUnitMappingServiceImpl(repository, apiResponseFactory);

    @Test
    void saveDepartmentUnitMapping_happyPath() {
        DepartmentUnitMappingRequestDto dto = new DepartmentUnitMappingRequestDto();
        dto.setDepartmentId(1L);
        dto.setUnitId(2L);

        ResponseEntity<?> responseEntity = service.saveDepartmentUnitMapping(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateDepartmentUnitMapping_validationFailure_nullId() {
        DepartmentUnitMappingRequestDto dto = new DepartmentUnitMappingRequestDto();
        dto.setDepartmentId(1L);
        dto.setUnitId(2L);

        ResponseEntity<?> responseEntity = service.updateDepartmentUnitMapping(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Department unit mapping id is null", response.getMessage());
    }

    @Test
    void departmentUnitMappingDropDown_negative_emptyList() {
        when(repository.departmentUnitMappingDropDown()).thenReturn(List.of());

        ResponseEntity<?> responseEntity = service.departmentUnitMappingDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void departmentUnitMappingList_validationFailure_negativePage() {
        DepartmentUnitMappingListRequestDto dto = new DepartmentUnitMappingListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);

        ResponseEntity<?> responseEntity = service.departmentUnitMappingList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void deleteDepartmentUnitMapping_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteDepartmentUnitMapping(1L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).departmentUnitMappingDelete(1L);
    }
}
