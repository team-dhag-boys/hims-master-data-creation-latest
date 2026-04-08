package com.hims.masters.common.controller;

import com.hims.masters.common.services.TalukaService;
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

@WebMvcTest(TalukaController.class)
@Import(ApiResponseFactory.class)
class TalukaControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TalukaService talukaService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllTalukaApis() throws Exception {
        doReturn(okResponse()).when(talukaService).saveTaluka(any());
        doReturn(okResponse()).when(talukaService).updateTaluka(any());
        doReturn(okResponse()).when(talukaService).getTalukaById(anyLong());
        doReturn(okResponse()).when(talukaService).autocomplete(anyString());
        doReturn(okResponse()).when(talukaService).talukaDropDown();
        doReturn(okResponse()).when(talukaService).talukaList(any());
        doReturn(okResponse()).when(talukaService).deleteTaluka(anyLong());

        mockMvc.perform(post("/taluka/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"talukaCode\":\"HVL\",\"talukaName\":\"Haveli\",\"districtId\":1}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/taluka/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"talukaCode\":\"HVL\",\"talukaName\":\"Haveli\",\"districtId\":1,\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/taluka/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/taluka/autocomplete").param("searchString", "Hav"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/taluka/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/taluka/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"Hav\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/taluka/delete/1"))
                .andExpect(status().isOk());
    }
}
