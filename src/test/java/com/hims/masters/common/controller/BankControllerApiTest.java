package com.hims.masters.common.controller;

import com.hims.masters.common.services.BankService;
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

@WebMvcTest(BankController.class)
@Import(ApiResponseFactory.class)
class BankControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankService bankService;

    private ResponseEntity<?> okResponse() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Test
    void testAllBankApis() throws Exception {
        doReturn(okResponse()).when(bankService).saveBank(any());
        doReturn(okResponse()).when(bankService).updateBank(any());
        doReturn(okResponse()).when(bankService).getBankById(anyLong());
        doReturn(okResponse()).when(bankService).autocomplete(anyString());
        doReturn(okResponse()).when(bankService).bankDropDown();
        doReturn(okResponse()).when(bankService).bankList(any());
        doReturn(okResponse()).when(bankService).deleteBank(anyLong());

        mockMvc.perform(post("/bank/save")
                        .contentType(APPLICATION_JSON)
                        .content("{\"bankCode\":\"SBI\",\"bankName\":\"State Bank\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/bank/update")
                        .contentType(APPLICATION_JSON)
                        .content("{\"id\":1,\"bankCode\":\"SBI\",\"bankName\":\"State Bank\",\"active\":true}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/bank/getById/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/bank/autocomplete").param("searchString", "SB"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/bank/dropdown"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/bank/list")
                        .contentType(APPLICATION_JSON)
                        .content("{\"page\":0,\"size\":20,\"searchString\":\"SB\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/bank/delete/1"))
                .andExpect(status().isOk());
    }
}
