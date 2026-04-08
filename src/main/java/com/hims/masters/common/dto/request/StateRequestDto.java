package com.hims.masters.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StateRequestDto {
    private Long id;

    @NotBlank(message = "State code can not be blank")
    private String stateCode;

    @NotBlank(message = "State name can not be blank")
    private String stateName;

    @NotNull(message = "Country id can not be null")
    private Long countryId;

    private Boolean active = true;
}
