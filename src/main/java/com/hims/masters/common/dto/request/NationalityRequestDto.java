package com.hims.masters.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NationalityRequestDto {
    private Long id;

    @NotBlank(message = "Nationality code can not be blank")
    private String nationalityCode;

    @NotBlank(message = "Nationality name can not be blank")
    private String nationalityName;

    private Boolean active = true;
}
