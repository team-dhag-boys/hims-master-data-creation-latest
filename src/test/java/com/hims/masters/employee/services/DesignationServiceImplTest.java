package com.hims.masters.employee.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.employee.dto.request.DesignationListRequestDto;
import com.hims.masters.employee.dto.request.DesignationRequestDto;
import com.hims.masters.employee.repository.DesignationRepository;
import com.hims.masters.employee.services.impl.DesignationServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DesignationServiceImplTest {
    private final DesignationRepository repository = mock(DesignationRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final DesignationServiceImpl service = new DesignationServiceImpl(repository, apiResponseFactory);

    @Test
    void saveDesignation_happyPath() {
        DesignationRequestDto dto = new DesignationRequestDto();
        dto.setDesignation("Consultant");
        dto.setCode("CNS");

        ResponseEntity<?> responseEntity = service.saveDesignation(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateDesignation_validationFailure_nullId() {
        DesignationRequestDto dto = new DesignationRequestDto();
        dto.setDesignation("Consultant");
        dto.setCode("CNS");

        ResponseEntity<?> responseEntity = service.updateDesignation(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Designation id is null", response.getMessage());
    }

    @Test
    void designationDropDown_negative_emptyList() {
        when(repository.designationDropDown()).thenReturn(List.of());

        ResponseEntity<?> responseEntity = service.designationDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void designationList_validationFailure_negativePage() {
        DesignationListRequestDto dto = new DesignationListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);

        ResponseEntity<?> responseEntity = service.designationList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void deleteDesignation_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteDesignation(1L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).designationDelete(1L);
    }
}
