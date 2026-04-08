package com.hims.masters.common.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.common.dto.request.DepartmentListRequestDto;
import com.hims.masters.common.dto.request.DepartmentRequestDto;
import com.hims.masters.common.repository.DepartmentRepository;
import com.hims.masters.common.services.impl.DepartmentServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DepartmentServiceImplTest {
    private final DepartmentRepository repository = mock(DepartmentRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final DepartmentServiceImpl service = new DepartmentServiceImpl(repository, apiResponseFactory);

    @Test
    void saveDepartment_happyPath() {
        DepartmentRequestDto dto = new DepartmentRequestDto();
        dto.setDepartmentCode("MED");
        dto.setDepartmentName("Medicine");

        ResponseEntity<?> responseEntity = service.saveDepartment(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateDepartment_validationFailure_nullId() {
        DepartmentRequestDto dto = new DepartmentRequestDto();
        dto.setDepartmentCode("MED");
        dto.setDepartmentName("Medicine");

        ResponseEntity<?> responseEntity = service.updateDepartment(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Department id is null", response.getMessage());
    }

    @Test
    void departmentDropDown_negative_emptyList() {
        when(repository.departmentDropDown()).thenReturn(List.of());

        ResponseEntity<?> responseEntity = service.departmentDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void departmentList_validationFailure_negativePage() {
        DepartmentListRequestDto dto = new DepartmentListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);

        ResponseEntity<?> responseEntity = service.departmentList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void deleteDepartment_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteDepartment(1L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).departmentDelete(1L);
    }
}
