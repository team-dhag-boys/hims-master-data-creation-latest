package com.hims.masters.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PinCodeRequestDto {
    private Long id;

    @NotBlank(message = "Pincode can not be blank")
    private String pincode;

    @NotNull(message = "City id can not be null")
    private Long cityId;

    private Boolean active = true;
}
