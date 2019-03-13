package com.xuyun.platform.modules.common.dto.ky;

import lombok.Data;

import java.util.List;

@Data
public class KyDataReponse {
    private List<KyBetRecordReponse> list;
    private Integer code;
}
