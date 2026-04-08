package com.hims.masters.employee.entity;

import com.hims.masters.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "mt_employee_category")
public class EmployeeCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean active;


    private String category;

    private String categoryCode;

    private Boolean deleteFlag;

}
