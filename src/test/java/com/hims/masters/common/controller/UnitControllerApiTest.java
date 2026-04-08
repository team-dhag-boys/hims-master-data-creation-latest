package com.hims.masters.common.controller;

import com.hims.masters.common.services.UnitService;
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

@WebMvcTest(UnitController.class)
@Import(ApiResponseFactory.class)
class UnitControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UnitService unitService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllUnitApis() throws Exception {
        doReturn(okResponse()).when(unitService).saveUnit(any());
        doReturn(okResponse()).when(unitService).updateUnit(any());
        doReturn(okResponse()).when(unitService).getUnitById(anyLong());
        doReturn(okResponse()).when(unitService).autocomplete(anyString());
        doReturn(okResponse()).when(unitService).unitDropDown();
        doReturn(okResponse()).when(unitService).unitList(any());
        doReturn(okResponse()).when(unitService).deleteUnit(anyLong());

        mockMvc.perform(post("/unit/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"unitCode\":\"OPD\",\"unitName\":\"Out Patient\",\"organizationId\":1}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/unit/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"unitCode\":\"OPD\",\"unitName\":\"Out Patient\",\"organizationId\":1,\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/unit/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/unit/autocomplete").param("searchString", "OP"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/unit/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/unit/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"OP\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/unit/delete/1"))
                .andExpect(status().isOk());
    }
}
