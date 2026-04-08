package com.hims.masters.exception;

import com.hims.masters.utils.ApiResponseFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final ApiResponseFactory apiResponseFactory;

    public GlobalExceptionHandler(ApiResponseFactory apiResponseFactory) {
        this.apiResponseFactory = apiResponseFactory;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Throwable root = getRootCause(ex);
        if (root instanceof SQLException sqlException && "23505".equals(sqlException.getSQLState())) {
            return apiResponseFactory.conflict(buildUniqueConstraintMessage(sqlException));
        }
        return apiResponseFactory.badRequest("Data integrity violation");
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> handleSQLException(SQLException ex) {
        if ("23505".equals(ex.getSQLState())) {
            return apiResponseFactory.conflict(buildUniqueConstraintMessage(ex));
        }
        return apiResponseFactory.badRequest("Database operation failed");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return apiResponseFactory.error("Internal server error");
    }

    private String buildUniqueConstraintMessage(SQLException ex) {
        String message = ex.getMessage();
        if (message != null && message.contains("Detail:")) {
            String detail = message.substring(message.indexOf("Detail:") + "Detail:".length()).trim();
            if (!detail.isBlank()) {
                return "Duplicate value: " + detail;
            }
        }
        return "Duplicate value violates unique constraint";
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable root = throwable;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        return root;
    }
}
