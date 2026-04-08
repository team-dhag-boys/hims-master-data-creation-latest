package com.hims.masters.employee.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeTypeListRequestDto {
    private Integer page;
    private Integer size;
    private String searchString;
}
