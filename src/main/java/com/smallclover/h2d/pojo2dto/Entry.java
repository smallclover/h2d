package com.smallclover.h2d.pojo2dto;

import lombok.Data;

@Data
public class Entry {
    private Integer cPersonid;
    private String cName;
    private String cNameChn;
    private Integer cSequence;
    private String  cExamRank;
    private Integer cYear;
    private Integer cAge;
    private Integer cNianhaoId;
    private Integer cEntryNhYear;
    private Integer cEntryRange;
    private Integer cInstCode;
    private Integer cInstNameCode;
    private String  cExamField;
    private Integer cEntryAddrId;
    private Integer cParentalStatus;
    private Integer cAttemptCount;
    private String  cNotes;
    private String  cPostingNotes;
    private String  cEntryDescChn;
    private String cEntryTypeDescChn;
    private Float  cEntryTypeLevel;
}
