package com.hims.masters.common.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitListRequestDto {
    private Integer page;
    private Integer size;
    private String searchString;
}
