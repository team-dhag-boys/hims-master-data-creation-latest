package com.hims.masters.apiresponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T>  implements Serializable {

    private String message;

    private T result;

    private Integer statusCode;

    private Map<String, Object> map;

    private Long count;
}
