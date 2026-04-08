package com.hims.masters.employee.controller;

import com.hims.masters.employee.services.DesignationService;
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

@WebMvcTest(DesignationController.class)
@Import(ApiResponseFactory.class)
class DesignationControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DesignationService designationService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllDesignationApis() throws Exception {
        doReturn(okResponse()).when(designationService).saveDesignation(any());
        doReturn(okResponse()).when(designationService).updateDesignation(any());
        doReturn(okResponse()).when(designationService).getDesignationById(anyLong());
        doReturn(okResponse()).when(designationService).autocomplete(anyString());
        doReturn(okResponse()).when(designationService).designationDropDown();
        doReturn(okResponse()).when(designationService).designationList(any());
        doReturn(okResponse()).when(designationService).deleteDesignation(anyLong());

        mockMvc.perform(post("/designation/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"designation\":\"Consultant\",\"code\":\"CNS\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/designation/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"designation\":\"Consultant\",\"code\":\"CNS\",\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/designation/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/designation/autocomplete").param("searchString", "Con"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/designation/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/designation/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"Con\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/designation/delete/1"))
                .andExpect(status().isOk());
    }
}
