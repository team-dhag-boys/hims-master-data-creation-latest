package com.hims.masters.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QualificationRequestDto {
    private Long id;

    @NotBlank(message = "Qualification code can not be blank")
    private String qualificationCode;

    @NotBlank(message = "Qualification name can not be blank")
    private String qualificationName;

    private Boolean active = true;
}
