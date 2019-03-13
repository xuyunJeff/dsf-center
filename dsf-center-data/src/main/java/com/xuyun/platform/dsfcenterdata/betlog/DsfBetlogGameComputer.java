package com.xuyun.platform.dsfcenterdata.betlog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "dsf_betlog_game_computer")
@NoArgsConstructor
@AllArgsConstructor
public class DsfBetlogGameComputer extends AbstractDsfBetlog {
    private String tableName;
    @Transient
    @JsonIgnore
    private String month;
    //局ID
    private String issueNo;
}
