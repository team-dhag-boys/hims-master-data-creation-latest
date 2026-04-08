package com.hims.masters.common.controller;

import com.hims.masters.common.services.AreaService;
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

@WebMvcTest(AreaController.class)
@Import(ApiResponseFactory.class)
class AreaControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AreaService areaService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllAreaApis() throws Exception {
        doReturn(okResponse()).when(areaService).saveArea(any());
        doReturn(okResponse()).when(areaService).updateArea(any());
        doReturn(okResponse()).when(areaService).getAreaById(anyLong());
        doReturn(okResponse()).when(areaService).autocomplete(anyString());
        doReturn(okResponse()).when(areaService).areaDropDown();
        doReturn(okResponse()).when(areaService).areaList(any());
        doReturn(okResponse()).when(areaService).deleteArea(anyLong());

        mockMvc.perform(post("/area/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"areaCode\":\"KOT\",\"areaName\":\"Kothrud\",\"pinCodeId\":1}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/area/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"areaCode\":\"KOT\",\"areaName\":\"Kothrud\",\"pinCodeId\":1,\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/area/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/area/autocomplete").param("searchString", "Kot"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/area/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/area/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"Kot\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/area/delete/1"))
                .andExpect(status().isOk());
    }
}
