package com.smallclover.h2d.pojo2dto;

import lombok.Data;

@Data
public class Person {
    private Integer cPersonid;
    private String cName;
    private String cNameChn;
    private String cAltNameChn;
    private Integer cIndexYear;
    private Boolean cFemale;
    private Integer cBirthyear;
    private Integer cDeathyear;
    private String cDynastyChn;

}