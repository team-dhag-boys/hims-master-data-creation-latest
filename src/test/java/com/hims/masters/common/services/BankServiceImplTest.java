package com.hims.masters.common.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.common.dto.request.BankListRequestDto;
import com.hims.masters.common.dto.request.BankRequestDto;
import com.hims.masters.common.repository.BankRepository;
import com.hims.masters.common.services.impl.BankServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BankServiceImplTest {
    private final BankRepository repository = mock(BankRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final BankServiceImpl service = new BankServiceImpl(repository, apiResponseFactory);

    @Test
    void saveBank_happyPath() {
        BankRequestDto dto = new BankRequestDto();
        dto.setBankCode("SBI");
        dto.setBankName("State Bank");

        ResponseEntity<?> responseEntity = service.saveBank(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateBank_validationFailure_nullId() {
        BankRequestDto dto = new BankRequestDto();
        dto.setBankCode("SBI");
        dto.setBankName("State Bank");

        ResponseEntity<?> responseEntity = service.updateBank(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Bank id is null", response.getMessage());
    }

    @Test
    void bankDropDown_negative_emptyList() {
        when(repository.bankDropDown()).thenReturn(List.of());

        ResponseEntity<?> responseEntity = service.bankDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void bankList_validationFailure_negativePage() {
        BankListRequestDto dto = new BankListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);

        ResponseEntity<?> responseEntity = service.bankList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void deleteBank_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteBank(1L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).bankDelete(1L);
    }
}
