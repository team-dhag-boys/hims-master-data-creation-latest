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
@Table(name = "mt_country")
public class Country extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Country code can not be blank")
    @Column(name = "country_code", length = 100, unique = true)
    private String countryCode;

    @NotBlank(message = "Country name can not be blank")
    @Column(name = "country_name", length = 100, unique = true)
    private String countryName;

    @NotBlank(message = "ISD code can not be blank")
    @Column(name = "isd_code", length = 10, unique = true)
    private String isdCode;

    @Column(name = "mobile_length", length = 15)
    private String mobileLength;

    @NotNull(message = "active status can not be null")
    private Boolean active = true;

    private Boolean deleteFlag = false;

    @Column(name = "is_default")
    private Boolean isDefault = false;
}
