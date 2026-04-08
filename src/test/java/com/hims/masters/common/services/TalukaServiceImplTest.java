package com.hims.masters.common.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.common.dto.request.TalukaListRequestDto;
import com.hims.masters.common.dto.request.TalukaRequestDto;
import com.hims.masters.common.repository.TalukaRepository;
import com.hims.masters.common.services.impl.TalukaServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TalukaServiceImplTest {
    private final TalukaRepository repository = mock(TalukaRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final TalukaServiceImpl service = new TalukaServiceImpl(repository, apiResponseFactory);

    @Test
    void saveTaluka_happyPath() {
        TalukaRequestDto dto = new TalukaRequestDto();
        dto.setTalukaCode("HVL");
        dto.setTalukaName("Haveli");
        dto.setDistrictId(1L);

        ResponseEntity<?> responseEntity = service.saveTaluka(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateTaluka_validationFailure_nullId() {
        TalukaRequestDto dto = new TalukaRequestDto();
        dto.setTalukaCode("HVL");
        dto.setTalukaName("Haveli");
        dto.setDistrictId(1L);

        ResponseEntity<?> responseEntity = service.updateTaluka(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Taluka id is null", response.getMessage());
    }

    @Test
    void talukaDropDown_negative_emptyList() {
        when(repository.talukaDropDown()).thenReturn(List.of());

        ResponseEntity<?> responseEntity = service.talukaDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void talukaList_validationFailure_negativePage() {
        TalukaListRequestDto dto = new TalukaListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);

        ResponseEntity<?> responseEntity = service.talukaList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void deleteTaluka_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteTaluka(1L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).talukaDelete(1L);
    }
}
