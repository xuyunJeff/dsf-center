package com.xuyun.platform.dsfcenterdata.betlog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "dsf_betlog_game_fish")
@NoArgsConstructor
@AllArgsConstructor
public class DsfBetlogGameFish extends AbstractDsfBetlog {
    private String tableName;
    @Transient
    @JsonIgnore
    private String month;
   //局ID
    private String issueNo;
}
