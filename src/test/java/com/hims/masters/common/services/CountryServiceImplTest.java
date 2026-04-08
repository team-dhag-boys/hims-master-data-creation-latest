package com.hims.masters.common.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.common.dto.request.CountryListRequestDto;
import com.hims.masters.common.dto.request.CountryRequestDto;
import com.hims.masters.common.repository.CountryRepository;
import com.hims.masters.common.services.impl.CountryServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CountryServiceImplTest {
    private final CountryRepository repository = mock(CountryRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final CountryServiceImpl service = new CountryServiceImpl(repository, apiResponseFactory);

    @Test
    void saveCountry_happyPath() {
        CountryRequestDto dto = new CountryRequestDto();
        dto.setCountryCode("IN");
        dto.setCountryName("India");
        dto.setIsdCode("+91");

        ResponseEntity<?> responseEntity = service.saveCountry(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateCountry_validationFailure_nullId() {
        CountryRequestDto dto = new CountryRequestDto();
        dto.setCountryCode("IN");
        dto.setCountryName("India");
        dto.setIsdCode("+91");

        ResponseEntity<?> responseEntity = service.updateCountry(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Country id is null", response.getMessage());
    }

    @Test
    void countryDropDown_negative_emptyList() {
        when(repository.countryDropDown()).thenReturn(List.of());

        ResponseEntity<?> responseEntity = service.countryDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void countryList_validationFailure_negativePage() {
        CountryListRequestDto dto = new CountryListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);

        ResponseEntity<?> responseEntity = service.countryList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void deleteCountry_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteCountry(1L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).countryDelete(1L);
    }
}
