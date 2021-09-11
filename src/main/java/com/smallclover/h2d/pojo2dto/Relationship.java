package com.smallclover.h2d.pojo2dto;

import lombok.Data;

@Data
public class Relationship {
    private Integer cPersonid;
    private String  cName;
    private String  cNameChn;
    private String cAssocTypeDescChn;
    private String cAssocDescChn;
}
