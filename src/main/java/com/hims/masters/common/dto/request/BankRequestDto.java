package com.hims.masters.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankRequestDto {
    private Long id;

    @NotBlank(message = "Bank code can not be blank")
    private String bankCode;

    @NotBlank(message = "Bank name can not be blank")
    private String bankName;

    private Boolean active = true;
}
