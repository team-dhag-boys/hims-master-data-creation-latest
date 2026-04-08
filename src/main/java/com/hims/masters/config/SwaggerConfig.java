package com.hims.masters.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SwaggerConfig {

    private static final Map<String, Integer> OPERATION_ORDER = buildOperationOrder();

    @Bean
    public OpenApiCustomizer customOperationSequenceCustomizer() {
        return this::reorderPaths;
    }

    private void reorderPaths(OpenAPI openApi) {
        Paths existingPaths = openApi.getPaths();
        if (existingPaths == null || existingPaths.isEmpty()) {
            return;
        }

        List<Map.Entry<String, io.swagger.v3.oas.models.PathItem>> entries = new ArrayList<>(existingPaths.entrySet());
        entries.sort(
                Comparator.comparing((Map.Entry<String, io.swagger.v3.oas.models.PathItem> entry) -> extractControllerPath(entry.getKey()))
                        .thenComparingInt(entry -> operationRank(entry.getKey()))
                        .thenComparing(Map.Entry::getKey)
        );

        Paths orderedPaths = new Paths();
        for (Map.Entry<String, io.swagger.v3.oas.models.PathItem> entry : entries) {
            orderedPaths.addPathItem(entry.getKey(), entry.getValue());
        }
        openApi.setPaths(orderedPaths);
    }

    private static Map<String, Integer> buildOperationOrder() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("save", 1);
        map.put("list", 2);
        map.put("getById", 3);
        map.put("dropdown", 4);
        map.put("autocomplete", 5);
        map.put("update", 6);
        map.put("delete", 7);
        return map;
    }

    private String extractControllerPath(String fullPath) {
        if (fullPath == null || fullPath.isBlank()) {
            return "";
        }

        String normalized = fullPath.startsWith("/") ? fullPath.substring(1) : fullPath;
        int firstSlash = normalized.indexOf('/');
        if (firstSlash < 0) {
            return "/" + normalized;
        }
        return "/" + normalized.substring(0, firstSlash);
    }

    private int operationRank(String fullPath) {
        String operation = extractOperation(fullPath);
        return OPERATION_ORDER.getOrDefault(operation, Integer.MAX_VALUE);
    }

    private String extractOperation(String fullPath) {
        int lastSlash = fullPath.lastIndexOf('/');
        if (lastSlash < 0 || lastSlash == fullPath.length() - 1) {
            return fullPath;
        }

        String tail = fullPath.substring(lastSlash + 1);
        // Handles /getById/{id} and /delete/{id}
        if (tail.startsWith("{")) {
            String base = fullPath.substring(0, lastSlash);
            int previousSlash = base.lastIndexOf('/');
            if (previousSlash >= 0 && previousSlash < base.length() - 1) {
                return base.substring(previousSlash + 1);
            }
        }
        return tail;
    }
}
