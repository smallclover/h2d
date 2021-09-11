package com.smallclover.h2d.pojo2dto;

import lombok.Data;

@Data
public class Status {
    private Integer cPersonid;
    private String cName;
    private String cNameChn;
    private Integer cFirstyear;
    private Integer cLastyear;
    private String  cSupplement;
    private String  cNotes;
    private String cStatusTypeChn;
    private String cStatusDescChn;
}
