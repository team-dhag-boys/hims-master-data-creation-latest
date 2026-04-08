package com.hims.masters.common.controller;

import com.hims.masters.common.services.MaritalStatusService;
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

@WebMvcTest(MaritalStatusController.class)
@Import(ApiResponseFactory.class)
class MaritalStatusControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MaritalStatusService maritalStatusService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllMaritalStatusApis() throws Exception {
        doReturn(okResponse()).when(maritalStatusService).saveMaritalStatus(any());
        doReturn(okResponse()).when(maritalStatusService).updateMaritalStatus(any());
        doReturn(okResponse()).when(maritalStatusService).getMaritalStatusById(anyLong());
        doReturn(okResponse()).when(maritalStatusService).autocomplete(anyString());
        doReturn(okResponse()).when(maritalStatusService).maritalStatusDropDown();
        doReturn(okResponse()).when(maritalStatusService).maritalStatusList(any());
        doReturn(okResponse()).when(maritalStatusService).deleteMaritalStatus(anyLong());

        mockMvc.perform(post("/maritalStatus/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"maritalStatusCode\":\"M\",\"maritalStatusName\":\"Married\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/maritalStatus/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"maritalStatusCode\":\"M\",\"maritalStatusName\":\"Married\",\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/maritalStatus/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/maritalStatus/autocomplete").param("searchString", "Mar"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/maritalStatus/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/maritalStatus/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"Mar\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/maritalStatus/delete/1"))
                .andExpect(status().isOk());
    }
}
