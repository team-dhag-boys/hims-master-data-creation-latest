package com.hims.masters.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaritalStatusRequestDto {
    private Long id;

    @NotBlank(message = "Marital status code can not be blank")
    private String maritalStatusCode;

    @NotBlank(message = "Marital status name can not be blank")
    private String maritalStatusName;

    private Boolean active = true;
}
