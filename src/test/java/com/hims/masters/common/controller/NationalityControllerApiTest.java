package com.hims.masters.common.controller;

import com.hims.masters.common.services.NationalityService;
import com.hims.masters.utils.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NationalityController.class)
@Import(ApiResponseFactory.class)
class NationalityControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NationalityService nationalityService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllNationalityApis() throws Exception {
        doReturn(okResponse()).when(nationalityService).saveNationality(any());
        doReturn(okResponse()).when(nationalityService).updateNationality(any());
        doReturn(okResponse()).when(nationalityService).getNationalityById(anyLong());
        doReturn(okResponse()).when(nationalityService).autocomplete(anyString());
        doReturn(okResponse()).when(nationalityService).nationalityDropDown();
        doReturn(okResponse()).when(nationalityService).nationalityList(any());
        doReturn(okResponse()).when(nationalityService).deleteNationality(anyLong());

        mockMvc.perform(post("/nationality/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"nationalityCode\":\"IND\",\"nationalityName\":\"Indian\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/nationality/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"nationalityCode\":\"IND\",\"nationalityName\":\"Indian\",\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/nationality/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/nationality/autocomplete").param("searchString", "Ind"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/nationality/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/nationality/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"Ind\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/nationality/delete/1"))
                .andExpect(status().isOk());
    }
}
