package com.invech.platform.modules.common.dto.fg;

import lombok.Data;
import java.util.List;

@Data
public class FgBetResponse {
    private List<FgBetlogResponse> data;
    private String page_key;
}
