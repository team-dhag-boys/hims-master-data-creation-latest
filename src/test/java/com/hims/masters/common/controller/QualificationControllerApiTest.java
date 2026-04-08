package com.hims.masters.common.controller;

import com.hims.masters.common.services.QualificationService;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QualificationController.class)
@Import(ApiResponseFactory.class)
class QualificationControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QualificationService qualificationService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllQualificationApis() throws Exception {
        doReturn(okResponse()).when(qualificationService).saveQualification(any());
        doReturn(okResponse()).when(qualificationService).updateQualification(any());
        doReturn(okResponse()).when(qualificationService).getQualificationById(anyLong());
        doReturn(okResponse()).when(qualificationService).autocomplete(anyString());
        doReturn(okResponse()).when(qualificationService).qualificationDropDown();
        doReturn(okResponse()).when(qualificationService).qualificationList(any());
        doReturn(okResponse()).when(qualificationService).deleteQualification(anyLong());

        mockMvc.perform(post("/qualification/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"qualificationCode\":\"MBBS\",\"qualificationName\":\"MBBS\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/qualification/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"qualificationCode\":\"MBBS\",\"qualificationName\":\"MBBS\",\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/qualification/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/qualification/autocomplete").param("searchString", "MBB"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/qualification/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/qualification/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"MBB\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/qualification/delete/1"))
                .andExpect(status().isOk());
    }
}
