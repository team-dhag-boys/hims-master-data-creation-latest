package com.hims.masters.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hims.masters.employee.entity.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private Employee createdBy;

    @Column(name = "created_date")
    @NotNull(message = "created date can not be null")
    private Timestamp createdDate = new Timestamp(System.currentTimeMillis());

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by")
    private Employee lastModifiedBy;

    @Column(name = "last_modified_date")
    @NotNull(message = "last modified date can not be null")
    private Timestamp lastModifiedDate = new Timestamp(System.currentTimeMillis());
}

