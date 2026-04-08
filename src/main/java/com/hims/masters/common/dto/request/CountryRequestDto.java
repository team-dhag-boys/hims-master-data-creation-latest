package com.hims.masters.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryRequestDto {
    private Long id;

    @NotBlank(message = "Country code can not be blank")
    private String countryCode;

    @NotBlank(message = "Country name can not be blank")
    private String countryName;

    @NotBlank(message = "ISD code can not be blank")
    private String isdCode;

    private String mobileLength;
    private Boolean active = true;
    private Boolean isDefault = false;
}
