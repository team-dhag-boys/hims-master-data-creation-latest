package com.hims.masters.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class BloodGroupReferenceDataInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BloodGroupReferenceDataInitializer.class);

    private final JdbcTemplate jdbcTemplate;

    public BloodGroupReferenceDataInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void ensureReferenceData() {
        try {
            Boolean tableExists = jdbcTemplate.queryForObject(
                    "select to_regclass('public.mt_blood_group') is not null",
                    Boolean.class
            );
            if (!Boolean.TRUE.equals(tableExists)) {
                LOGGER.info("Skipping blood-group seed. Table public.mt_blood_group does not exist yet.");
                return;
            }

            upsertBloodGroup("A+", "A Positive");
            upsertBloodGroup("A-", "A Negative");
            upsertBloodGroup("B+", "B Positive");
            upsertBloodGroup("B-", "B Negative");
            upsertBloodGroup("AB+", "AB Positive");
            upsertBloodGroup("AB-", "AB Negative");
            upsertBloodGroup("O+", "O Positive");
            upsertBloodGroup("O-", "O Negative");

            jdbcTemplate.update(
                    "update mt_blood_group " +
                            "set active = true, delete_flag = false, last_modified_date = now() " +
                            "where blood_group_code in ('A+','A-','B+','B-','AB+','AB-','O+','O-')"
            );
            LOGGER.info("Blood-group reference data ensured successfully.");
        } catch (Exception ex) {
            LOGGER.error("Failed to ensure blood-group reference data on startup.", ex);
        }
    }

    private void upsertBloodGroup(String code, String name) {
        jdbcTemplate.update(
                "insert into mt_blood_group (blood_group_code, blood_group_name, active, delete_flag, created_date, last_modified_date) " +
                        "select ?, ?, true, false, now(), now() " +
                        "where not exists (select 1 from mt_blood_group where blood_group_code = ?)",
                code, name, code
        );
    }
}
