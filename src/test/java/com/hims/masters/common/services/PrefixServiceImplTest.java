package com.hims.masters.common.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.common.dto.request.PrefixListRequestDto;
import com.hims.masters.common.dto.request.PrefixRequestDto;
import com.hims.masters.common.repository.PrefixRepository;
import com.hims.masters.common.services.impl.PrefixServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PrefixServiceImplTest {

    private final PrefixRepository repository = mock(PrefixRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final PrefixServiceImpl service = new PrefixServiceImpl(repository, apiResponseFactory);

    @Test
    void savePrefix_happyPath() {
        PrefixRequestDto dto = new PrefixRequestDto();
        dto.setPrefixCode("MR");
        dto.setPrefixName("Mister");

        ResponseEntity<?> responseEntity = service.savePrefix(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("Prefix saved", response.getMessage());
        verify(repository, times(1)).save(any());
    }

    @Test
    void prefixDropDown_negative_emptyList() {
        when(repository.getPrefixDropDown()).thenReturn(List.of());

        ResponseEntity<?> responseEntity = service.prefixDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
        assertEquals("Prefix List not found", response.getMessage());
    }

    @Test
    void prefixList_validationFailure_negativePage() {
        PrefixListRequestDto dto = new PrefixListRequestDto();
        dto.setPage(-1);
        dto.setSize(5);

        ResponseEntity<?> responseEntity = service.prefixList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Page should be greater than or equal to 0", response.getMessage());
    }

    @Test
    void deletePrefix_happyPath() {
        ResponseEntity<?> responseEntity = service.deletePrefix(5L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("Prefix deleted", response.getMessage());
        verify(repository, times(1)).prefixDelete(5L);
    }

    @Test
    void deletePrefix_validationFailure_nullId() {
        ResponseEntity<?> responseEntity = service.deletePrefix(null);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Prefix id is null", response.getMessage());
        verify(repository, never()).prefixDelete(any());
    }
}
