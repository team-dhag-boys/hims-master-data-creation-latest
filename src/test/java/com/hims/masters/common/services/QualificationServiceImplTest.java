package com.hims.masters.common.services;

import com.hims.masters.apiresponse.ApiResponse;
import com.hims.masters.common.dto.request.QualificationListRequestDto;
import com.hims.masters.common.dto.request.QualificationRequestDto;
import com.hims.masters.common.repository.QualificationRepository;
import com.hims.masters.common.services.impl.QualificationServiceImpl;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QualificationServiceImplTest {
    private final QualificationRepository repository = mock(QualificationRepository.class);
    private final ApiResponseFactory apiResponseFactory = new ApiResponseFactory();
    private final QualificationServiceImpl service = new QualificationServiceImpl(repository, apiResponseFactory);

    @Test
    void saveQualification_happyPath() {
        QualificationRequestDto dto = new QualificationRequestDto();
        dto.setQualificationCode("MBBS");
        dto.setQualificationName("MBBS");

        ResponseEntity<?> responseEntity = service.saveQualification(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateQualification_validationFailure_nullId() {
        QualificationRequestDto dto = new QualificationRequestDto();
        dto.setQualificationCode("MBBS");
        dto.setQualificationName("MBBS");

        ResponseEntity<?> responseEntity = service.updateQualification(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("Qualification id is null", response.getMessage());
    }

    @Test
    void qualificationDropDown_negative_emptyList() {
        when(repository.qualificationDropDown()).thenReturn(List.of());

        ResponseEntity<?> responseEntity = service.qualificationDropDown();
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void qualificationList_validationFailure_negativePage() {
        QualificationListRequestDto dto = new QualificationListRequestDto();
        dto.setPage(-1);
        dto.setSize(10);

        ResponseEntity<?> responseEntity = service.qualificationList(dto);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
    }

    @Test
    void deleteQualification_happyPath() {
        ResponseEntity<?> responseEntity = service.deleteQualification(1L);
        ApiResponse<?> response = (ApiResponse<?>) responseEntity.getBody();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        verify(repository, times(1)).qualificationDelete(1L);
    }
}
