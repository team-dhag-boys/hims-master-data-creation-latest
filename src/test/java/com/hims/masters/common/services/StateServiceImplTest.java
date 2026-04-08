package com.hims.masters.common.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.common.dto.request.StateListRequestDto;
import com.hims.masters.common.dto.request.StateRequestDto;
import com.hims.masters.common.repository.StateRepository;
import com.hims.masters.common.services.impl.StateServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StateServiceImplTest {
    private final StateRepository repository = mock(StateRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final StateServiceImpl service = new StateServiceImpl(repository, apiResponseFactory);

    @Test
    void saveState_happyPath() {
        StateRequestDto dto = new StateRequestDto();
        dto.setStateCode("MH");
        dto.setStateName("Maharashtra");
        dto.setCountryId(1L);

        ResponseEntity<?> responseEntity = service.saveState(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateState_validationFailure_nullId() {
        StateRequestDto dto = new StateRequestDto();
        dto.setStateCode("MH");
        dto.setStateName("Maharashtra");
        dto.setCountryId(1L);

        ResponseEntity<?> responseEntity = service.updateState(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("State id is null", response.getMessage());
    }

    @Test
    void stateDropDown_negative_emptyList() {
        when(repository.stateDropDown()).thenReturn(List.of());

        ResponseEntity<?> responseEntity = service.stateDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void stateList_validationFailure_negativePage() {
        StateListRequestDto dto = new StateListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);

        ResponseEntity<?> responseEntity = service.stateList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void deleteState_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteState(1L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).stateDelete(1L);
    }
}
