package com.hims.masters.common.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PrefixListRequestDto {

    private int page;

    private int size;

    private String searchString;

}
