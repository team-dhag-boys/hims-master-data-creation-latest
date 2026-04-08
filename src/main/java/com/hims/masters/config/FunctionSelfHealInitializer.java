package com.hims.masters.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class FunctionSelfHealInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FunctionSelfHealInitializer.class);

    private final JdbcTemplate jdbcTemplate;

    @Value("${hims.function-self-heal.enabled:true}")
    private boolean enabled;

    @Value("${hims.function-self-heal.paths:sqlscript,masters/sqlscript}")
    private String configuredPaths;

    @Value("${hims.function-self-heal.schema:public}")
    private String schema;

    public FunctionSelfHealInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void recreateMissingFunctions() {
        if (!enabled) {
            LOGGER.info("Function self-heal is disabled.");
            return;
        }

        List<Path> functionFiles = discoverFunctionSqlFiles();
        if (functionFiles.isEmpty()) {
            LOGGER.warn("Function self-heal found no SQL files in configured paths: {}", configuredPaths);
            return;
        }

        int recreatedCount = 0;
        for (Path file : functionFiles) {
            String functionName = functionNameFromFile(file);
            if (functionName.isEmpty()) {
                continue;
            }

            if (!functionExists(functionName)) {
                executeFunctionSql(file, functionName);
                recreatedCount++;
            }
        }

        LOGGER.info(
                "Function self-heal completed. scanned={}, recreated={}, schema={}",
                functionFiles.size(),
                recreatedCount,
                schema);
    }

    private List<Path> discoverFunctionSqlFiles() {
        List<Path> functionFiles = new ArrayList<>();
        String[] searchRoots = configuredPaths.split(",");
        for (String root : searchRoots) {
            String trimmedRoot = root.trim();
            if (trimmedRoot.isEmpty()) {
                continue;
            }

            Path rootPath = Paths.get(trimmedRoot);
            if (!Files.exists(rootPath) || !Files.isDirectory(rootPath)) {
                continue;
            }

            try (Stream<Path> stream = Files.walk(rootPath)) {
                stream.filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().startsWith("fn_mt_"))
                        .filter(path -> path.getFileName().toString().endsWith(".sql"))
                        .sorted(Comparator.comparing(Path::toString))
                        .forEach(functionFiles::add);
            } catch (IOException ex) {
                throw new IllegalStateException(
                        "Failed while scanning function SQL files under: " + rootPath, ex);
            }
        }
        return functionFiles;
    }

    private String functionNameFromFile(Path file) {
        String fileName = file.getFileName().toString();
        return fileName.substring(0, fileName.length() - ".sql".length());
    }

    private boolean functionExists(String functionName) {
        String query = """
                SELECT EXISTS (
                    SELECT 1
                    FROM pg_proc p
                    JOIN pg_namespace n ON n.oid = p.pronamespace
                    WHERE n.nspname = ? AND p.proname = ?
                )
                """;
        Boolean exists = jdbcTemplate.queryForObject(query, Boolean.class, schema, functionName);
        return Boolean.TRUE.equals(exists);
    }

    private void executeFunctionSql(Path file, String functionName) {
        try {
            String sql = Files.readString(file, StandardCharsets.UTF_8);
            jdbcTemplate.execute(sql);
            LOGGER.info("Function self-heal recreated missing function: {} from {}", functionName, file);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read SQL file for function: " + functionName, ex);
        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Failed to recreate missing function: " + functionName + " from file: " + file, ex);
        }
    }
}
