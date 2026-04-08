package com.hims.masters.common.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.common.dto.request.CityListRequestDto;
import com.hims.masters.common.dto.request.CityRequestDto;
import com.hims.masters.common.repository.CityRepository;
import com.hims.masters.common.services.impl.CityServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CityServiceImplTest {
    private final CityRepository repository = mock(CityRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final CityServiceImpl service = new CityServiceImpl(repository, apiResponseFactory);

    @Test
    void saveCity_happyPath() {
        CityRequestDto dto = new CityRequestDto();
        dto.setCityCode("PUNEC");
        dto.setCityName("Pune City");
        dto.setTalukaId(1L);

        ResponseEntity<?> responseEntity = service.saveCity(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateCity_validationFailure_nullId() {
        CityRequestDto dto = new CityRequestDto();
        dto.setCityCode("PUNEC");
        dto.setCityName("Pune City");
        dto.setTalukaId(1L);

        ResponseEntity<?> responseEntity = service.updateCity(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("City id is null", response.getMessage());
    }

    @Test
    void cityDropDown_negative_emptyList() {
        when(repository.cityDropDown()).thenReturn(List.of());

        ResponseEntity<?> responseEntity = service.cityDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void cityList_validationFailure_negativePage() {
        CityListRequestDto dto = new CityListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);

        ResponseEntity<?> responseEntity = service.cityList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void deleteCity_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteCity(1L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).cityDelete(1L);
    }
}
