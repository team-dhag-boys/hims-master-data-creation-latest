package com.hims.masters.common.controller;

import com.hims.masters.common.services.PrefixService;
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

@WebMvcTest(PrefixController.class)
@Import(ApiResponseFactory.class)
class PrefixControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrefixService prefixService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllPrefixApis() throws Exception {
        doReturn(okResponse()).when(prefixService).savePrefix(any());
        doReturn(okResponse()).when(prefixService).updatePrefix(any());
        doReturn(okResponse()).when(prefixService).getPrefixById(anyLong());
        doReturn(okResponse()).when(prefixService).autocomplete(anyString());
        doReturn(okResponse()).when(prefixService).prefixDropDown();
        doReturn(okResponse()).when(prefixService).prefixList(any());
        doReturn(okResponse()).when(prefixService).deletePrefix(anyLong());

        mockMvc.perform(post("/prefix/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"prefixCode\":\"MR\",\"prefixName\":\"Mister\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/prefix/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"prefixCode\":\"MR\",\"prefixName\":\"Mister\",\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/prefix/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/prefix/autocomplete").param("searchString", "M"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/prefix/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/prefix/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"M\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/prefix/delete/1"))
                .andExpect(status().isOk());
    }
}
