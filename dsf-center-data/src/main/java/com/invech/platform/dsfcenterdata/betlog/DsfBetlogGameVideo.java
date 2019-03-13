package com.invech.platform.dsfcenterdata.betlog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: DsfBetlogGameVideo
 * @Author: R.M.I
 * @CreateTime: 2019年02月28日 20:09:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Data
@Table(name = "dsf_betlog_game_video")
@NoArgsConstructor
@AllArgsConstructor
public class DsfBetlogGameVideo extends AbstractDsfBetlog {

    private String tableName;
    /**
     *  期号
     */
    private String issueNo;

    private String gameResult;

    private String resultPictureUrl;

    @Transient
    @JsonIgnore
    private String month;

}
