package com.hims.masters.employee.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeTypeRequestDto {
    private Long id;

    @NotBlank(message = "Employee type can not be blank")
    private String employeeType;

    @NotNull(message = "isClinical can not be null")
    private Boolean isClinical;

    private Boolean applicableToDoctorType = false;

    private Boolean isDoctor = false;

    private Boolean active = true;
}
