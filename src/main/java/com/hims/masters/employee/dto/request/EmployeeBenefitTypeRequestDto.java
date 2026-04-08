package com.hims.masters.employee.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeBenefitTypeRequestDto {
    private Long id;

    @NotBlank(message = "Benefit type code can not be blank")
    private String benefitTypeCode;

    @NotBlank(message = "Benefit type name can not be blank")
    private String benefitTypeName;

    private Boolean active = true;
}
