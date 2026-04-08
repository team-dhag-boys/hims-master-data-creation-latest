package com.hims.masters.common.controller;

import com.hims.masters.common.services.StateService;
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

@WebMvcTest(StateController.class)
@Import(ApiResponseFactory.class)
class StateControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StateService stateService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllStateApis() throws Exception {
        doReturn(okResponse()).when(stateService).saveState(any());
        doReturn(okResponse()).when(stateService).updateState(any());
        doReturn(okResponse()).when(stateService).getStateById(anyLong());
        doReturn(okResponse()).when(stateService).autocomplete(anyString());
        doReturn(okResponse()).when(stateService).stateDropDown();
        doReturn(okResponse()).when(stateService).stateList(any());
        doReturn(okResponse()).when(stateService).deleteState(anyLong());

        mockMvc.perform(post("/state/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"stateCode\":\"MH\",\"stateName\":\"Maharashtra\",\"countryId\":1}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/state/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"stateCode\":\"MH\",\"stateName\":\"Maharashtra\",\"countryId\":1,\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/state/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/state/autocomplete").param("searchString", "Mah"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/state/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/state/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"Mah\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/state/delete/1"))
                .andExpect(status().isOk());
    }
}
