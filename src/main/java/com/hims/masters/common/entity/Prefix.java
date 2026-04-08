package com.hims.masters.common.entity;

import com.hims.masters.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "mt_prefix")
public class Prefix extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Prefix code can not be blank")
    @Column(name = "prefix_code", length = 10, unique = true)
    private String prefixCode;

    @NotBlank(message = "Prefix Name can not be blank")
    @Column(name = "prefix_name", length = 15, unique = true)
    private String prefixName;

    @ManyToOne
    @JoinColumn(name = "gender_prefix")
    private Gender genderPrefix;

    @NotNull(message = "active status can not be null")
    private Boolean active = true;

    private Boolean deleteFlag = false;


}
