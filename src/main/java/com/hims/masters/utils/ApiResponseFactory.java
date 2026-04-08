package com.hims.masters.utils;

import com.hims.masters.apiresponse.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ApiResponseFactory {

    public ResponseEntity<ApiResponse<Object>> ok(String message) {
        return buildResponse(message, null, null, HttpStatus.OK.value());
    }

    public ResponseEntity<ApiResponse<Object>> ok(String message, Object result, Long count) {
        return buildResponse(message, result, count, HttpStatus.OK.value());
    }

    public ResponseEntity<ApiResponse<Object>> badRequest(String message) {
        return buildResponse(message, null, null, HttpStatus.BAD_REQUEST.value());
    }

    public ResponseEntity<ApiResponse<Object>> conflict(String message) {
        return buildResponse(message, null, null, HttpStatus.CONFLICT.value());
    }

    public ResponseEntity<ApiResponse<Object>> notFound(String message) {
        return buildResponse(message, null, null, HttpStatus.NOT_FOUND.value());
    }

    public ResponseEntity<ApiResponse<Object>> error(String message) {
        return buildResponse(message, null, null, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ResponseEntity<ApiResponse<Object>> buildResponse(String message, Object result, Long count, int statusCode) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setMessage(message);
        response.setResult(result);
        response.setCount(count);
        response.setStatusCode(statusCode);
        return ResponseEntity.ok(response);
    }
}
