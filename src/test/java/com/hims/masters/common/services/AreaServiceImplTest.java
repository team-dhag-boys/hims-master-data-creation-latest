package com.hims.masters.common.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.common.dto.request.AreaListRequestDto;
import com.hims.masters.common.dto.request.AreaRequestDto;
import com.hims.masters.common.repository.AreaRepository;
import com.hims.masters.common.services.impl.AreaServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AreaServiceImplTest {
    private final AreaRepository repository = mock(AreaRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final AreaServiceImpl service = new AreaServiceImpl(repository, apiResponseFactory);

    @Test
    void saveArea_happyPath() {
        AreaRequestDto dto = new AreaRequestDto();
        dto.setAreaCode("KOT");
        dto.setAreaName("Kothrud");
        dto.setPinCodeId(1L);

        ResponseEntity<?> responseEntity = service.saveArea(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateArea_validationFailure_nullId() {
        AreaRequestDto dto = new AreaRequestDto();
        dto.setAreaCode("KOT");
        dto.setAreaName("Kothrud");
        dto.setPinCodeId(1L);

        ResponseEntity<?> responseEntity = service.updateArea(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Area id is null", response.getMessage());
    }

    @Test
    void areaDropDown_negative_emptyList() {
        when(repository.areaDropDown()).thenReturn(List.of());

        ResponseEntity<?> responseEntity = service.areaDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void areaList_validationFailure_negativePage() {
        AreaListRequestDto dto = new AreaListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);

        ResponseEntity<?> responseEntity = service.areaList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void deleteArea_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteArea(1L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).areaDelete(1L);
    }
}
