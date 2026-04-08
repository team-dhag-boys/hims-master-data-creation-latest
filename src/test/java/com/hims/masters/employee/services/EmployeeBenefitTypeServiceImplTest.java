package com.hims.masters.employee.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.employee.dto.request.EmployeeBenefitTypeListRequestDto;
import com.hims.masters.employee.dto.request.EmployeeBenefitTypeRequestDto;
import com.hims.masters.employee.repository.EmployeeBenefitTypeRepository;
import com.hims.masters.employee.services.impl.EmployeeBenefitTypeServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeBenefitTypeServiceImplTest {
    private final EmployeeBenefitTypeRepository repository = mock(EmployeeBenefitTypeRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final EmployeeBenefitTypeServiceImpl service = new EmployeeBenefitTypeServiceImpl(repository, apiResponseFactory);

    @Test
    void saveEmployeeBenefitType_happyPath() {
        EmployeeBenefitTypeRequestDto dto = new EmployeeBenefitTypeRequestDto();
        dto.setBenefitTypeCode("PF");
        dto.setBenefitTypeName("Provident Fund");

        ResponseEntity<?> responseEntity = service.saveEmployeeBenefitType(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateEmployeeBenefitType_validationFailure_nullId() {
        EmployeeBenefitTypeRequestDto dto = new EmployeeBenefitTypeRequestDto();
        dto.setBenefitTypeCode("PF");
        dto.setBenefitTypeName("Provident Fund");

        ResponseEntity<?> responseEntity = service.updateEmployeeBenefitType(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Employee benefit type id is null", response.getMessage());
    }

    @Test
    void employeeBenefitTypeDropDown_negative_emptyList() {
        when(repository.employeeBenefitTypeDropDown()).thenReturn(List.of());

        ResponseEntity<?> responseEntity = service.employeeBenefitTypeDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void employeeBenefitTypeList_validationFailure_negativePage() {
        EmployeeBenefitTypeListRequestDto dto = new EmployeeBenefitTypeListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);

        ResponseEntity<?> responseEntity = service.employeeBenefitTypeList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void deleteEmployeeBenefitType_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteEmployeeBenefitType(1L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).employeeBenefitTypeDelete(1L);
    }
}
