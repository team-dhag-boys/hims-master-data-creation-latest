package com.hims.masters.employee.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.employee.dto.request.EmployeeCategoryListRequestDto;
import com.hims.masters.employee.dto.request.EmployeeCategoryRequestDto;
import com.hims.masters.employee.repository.EmployeeCategoryRepository;
import com.hims.masters.employee.services.impl.EmployeeCategoryServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeCategoryServiceImplTest {

    private final EmployeeCategoryRepository repository = mock(EmployeeCategoryRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final EmployeeCategoryServiceImpl service = new EmployeeCategoryServiceImpl(repository, apiResponseFactory);

    @Test
    void saveEmployeeCategory_happyPath() {
        EmployeeCategoryRequestDto dto = new EmployeeCategoryRequestDto();
        dto.setCategory("Clinical");
        dto.setCode("CLN");
        dto.setEmployeeTypeId(1L);
        dto.setActive(true);

        ResponseEntity<?> responseEntity = service.saveEmployeeCategory(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("Employee category saved", response.getMessage());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateEmployeeCategory_validationFailure_nullId() {
        EmployeeCategoryRequestDto dto = new EmployeeCategoryRequestDto();
        dto.setCategory("Clinical");
        dto.setCode("CLN");

        ResponseEntity<?> responseEntity = service.updateEmployeeCategory(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Employee category id is null", response.getMessage());
        verify(repository, never()).employeeCategoryUpdate(any(), any(), any(), any(), any());
    }

    @Test
    void employeeCategoryDropDown_negative_emptyList() {
        when(repository.employeeCategoryDropDown()).thenReturn(List.of());

        ResponseEntity<?> responseEntity = service.employeeCategoryDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
        assertEquals("Employee category list not found", response.getMessage());
    }

    @Test
    void employeeCategoryList_validationFailure_negativePage() {
        EmployeeCategoryListRequestDto dto = new EmployeeCategoryListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);

        ResponseEntity<?> responseEntity = service.employeeCategoryList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Page should be greater than or equal to 0", response.getMessage());
    }

    @Test
    void deleteEmployeeCategory_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteEmployeeCategory(11L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("Employee category deleted", response.getMessage());
        verify(repository, times(1)).employeeCategoryDelete(11L);
    }

    @Test
    void deleteEmployeeCategory_validationFailure_nullId() {
        ResponseEntity<?> responseEntity = service.deleteEmployeeCategory(null);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Employee category id is null", response.getMessage());
        verify(repository, never()).employeeCategoryDelete(any());
    }
}
