package com.hims.masters.common.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.common.dto.request.UnitListRequestDto;
import com.hims.masters.common.dto.request.UnitRequestDto;
import com.hims.masters.common.repository.UnitRepository;
import com.hims.masters.common.services.impl.UnitServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UnitServiceImplTest {
    private final UnitRepository repository = mock(UnitRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final UnitServiceImpl service = new UnitServiceImpl(repository, apiResponseFactory);

    @Test
    void saveUnit_happyPath() {
        UnitRequestDto dto = new UnitRequestDto();
        dto.setUnitCode("OPD");
        dto.setUnitName("Out Patient");
        dto.setOrganizationId(1L);

        ResponseEntity<?> responseEntity = service.saveUnit(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateUnit_validationFailure_nullId() {
        UnitRequestDto dto = new UnitRequestDto();
        dto.setUnitCode("OPD");
        dto.setUnitName("Out Patient");
        dto.setOrganizationId(1L);

        ResponseEntity<?> responseEntity = service.updateUnit(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Unit id is null", response.getMessage());
    }

    @Test
    void unitDropDown_negative_emptyList() {
        when(repository.unitDropDown()).thenReturn(List.of());

        ResponseEntity<?> responseEntity = service.unitDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void unitList_validationFailure_negativePage() {
        UnitListRequestDto dto = new UnitListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);

        ResponseEntity<?> responseEntity = service.unitList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void deleteUnit_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteUnit(1L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).unitDelete(1L);
    }
}
