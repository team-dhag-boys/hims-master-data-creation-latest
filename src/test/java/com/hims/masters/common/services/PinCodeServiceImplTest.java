package com.hims.masters.common.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.common.dto.request.PinCodeListRequestDto;
import com.hims.masters.common.dto.request.PinCodeRequestDto;
import com.hims.masters.common.repository.PinCodeRepository;
import com.hims.masters.common.services.impl.PinCodeServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PinCodeServiceImplTest {
    private final PinCodeRepository repository = mock(PinCodeRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final PinCodeServiceImpl service = new PinCodeServiceImpl(repository, apiResponseFactory);

    @Test
    void savePinCode_happyPath() {
        PinCodeRequestDto dto = new PinCodeRequestDto();
        dto.setPincode("411001");
        dto.setCityId(1L);

        ResponseEntity<?> responseEntity = service.savePinCode(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updatePinCode_validationFailure_nullId() {
        PinCodeRequestDto dto = new PinCodeRequestDto();
        dto.setPincode("411001");
        dto.setCityId(1L);

        ResponseEntity<?> responseEntity = service.updatePinCode(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Pincode id is null", response.getMessage());
    }

    @Test
    void pinCodeDropDown_negative_emptyList() {
        when(repository.pinCodeDropDown()).thenReturn(List.of());

        ResponseEntity<?> responseEntity = service.pinCodeDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void pinCodeList_validationFailure_negativePage() {
        PinCodeListRequestDto dto = new PinCodeListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);

        ResponseEntity<?> responseEntity = service.pinCodeList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void deletePinCode_happyPath() {
        ResponseEntity<?> responseEntity = service.deletePinCode(1L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).pinCodeDelete(1L);
    }
}
