package com.hims.masters.aop;

import com.hims.masters.apiresponse.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class ServiceLoggingAspect {
    private final ObjectMapper objectMapper;

    public ServiceLoggingAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around("execution(* com.hims.masters..services.impl..*(..))")
    public Object logServiceCall(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        if (shouldSkipLogging(method, joinPoint.getTarget().getClass())) {
            return joinPoint.proceed();
        }

        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        Object[] args = joinPoint.getArgs();
        long start = System.currentTimeMillis();

        log.info("SERVICE_REQUEST {}.{} args={}", className, methodName, serializeArgs(args));
        try {
            Object result = joinPoint.proceed();
            long durationMs = System.currentTimeMillis() - start;
            logServiceResponse(className, methodName, result, durationMs);
            return result;
        } catch (Exception ex) {
            long durationMs = System.currentTimeMillis() - start;
            log.error("SERVICE_ERROR {}.{} durationMs={} error={}", className, methodName, durationMs, ex.getMessage(), ex);
            throw ex;
        }
    }

    private void logServiceResponse(String className, String methodName, Object result, long durationMs) {
        if (result instanceof ResponseEntity<?> responseEntity && responseEntity.getBody() instanceof ApiResponse<?> body) {
            log.info("SERVICE_RESPONSE {}.{} durationMs={} responseBody={}",
                    className, methodName, durationMs, toJson(body));
            return;
        }
        log.info("SERVICE_RESPONSE {}.{} durationMs={} result={}", className, methodName, durationMs, toJson(result));
    }

    private boolean shouldSkipLogging(Method method, Class<?> targetClass) {
        if (method.isAnnotationPresent(NoServiceLog.class)) {
            return true;
        }
        return targetClass.isAnnotationPresent(NoServiceLog.class);
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return String.valueOf(value);
        }
    }

    private String serializeArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        String[] serialized = Arrays.stream(args)
                .map(this::toJson)
                .toArray(String[]::new);
        return Arrays.toString(serialized);
    }
}
