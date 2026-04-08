package com.hims.masters.employee.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.employee.dto.request.EmployeeTypeListRequestDto;
import com.hims.masters.employee.dto.request.EmployeeTypeRequestDto;
import com.hims.masters.employee.repository.EmployeeTypeRepository;
import com.hims.masters.employee.services.impl.EmployeeTypeServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeTypeServiceImplTest {

    private final EmployeeTypeRepository repository = mock(EmployeeTypeRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final EmployeeTypeServiceImpl service = new EmployeeTypeServiceImpl(repository, apiResponseFactory);

    @Test
    void saveEmployeeType_happyPath() {
        EmployeeTypeRequestDto dto = new EmployeeTypeRequestDto();
        dto.setEmployeeType("Consultant");
        dto.setIsClinical(true);
        dto.setApplicableToDoctorType(true);
        dto.setIsDoctor(true);
        dto.setActive(true);

        ResponseEntity<?> responseEntity = service.saveEmployeeType(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("Employee type saved", response.getMessage());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateEmployeeType_validationFailure_nullId() {
        EmployeeTypeRequestDto dto = new EmployeeTypeRequestDto();
        dto.setEmployeeType("Consultant");
        dto.setIsClinical(true);

        ResponseEntity<?> responseEntity = service.updateEmployeeType(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Employee type id is null", response.getMessage());
        verify(repository, never()).employeeTypeUpdate(any(), any(), any(), any(), any(), any());
    }

    @Test
    void employeeTypeDropDown_negative_emptyList() {
        when(repository.employeeTypeDropDown()).thenReturn(List.of());

        ResponseEntity<?> responseEntity = service.employeeTypeDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
        assertEquals("Employee type list not found", response.getMessage());
    }

    @Test
    void employeeTypeList_validationFailure_negativePage() {
        EmployeeTypeListRequestDto dto = new EmployeeTypeListRequestDto();
        dto.setPage(-1);
        dto.setSize(25);

        ResponseEntity<?> responseEntity = service.employeeTypeList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Page should be greater than or equal to 0", response.getMessage());
    }

    @Test
    void deleteEmployeeType_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteEmployeeType(10L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("Employee type deleted", response.getMessage());
        verify(repository, times(1)).employeeTypeDelete(10L);
    }

    @Test
    void deleteEmployeeType_validationFailure_nullId() {
        ResponseEntity<?> responseEntity = service.deleteEmployeeType(null);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Employee type id is null", response.getMessage());
        verify(repository, never()).employeeTypeDelete(any());
    }
}
