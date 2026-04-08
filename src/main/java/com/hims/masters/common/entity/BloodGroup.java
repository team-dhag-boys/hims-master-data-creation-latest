package com.hims.masters.common.entity;

import com.hims.masters.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "mt_blood_group")
public class BloodGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Blood group code can not be blank")
    @Column(name = "blood_group_code", length = 10, unique = true)
    private String bloodGroupCode;

    @NotBlank(message = "Blood group name can not be blank")
    @Column(name = "blood_group_name", length = 12, unique = true)
    private String bloodGroupName;

    @NotNull(message = "active status can not be null")
    private Boolean active = true;

    private Boolean deleteFlag = false;
}
