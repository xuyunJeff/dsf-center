package com.invech.platform.modules.common.dto.fg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FgBelogRequest {
    private String gt;
    private String id;
    private String page_key;

    @Override
    public String toString() {
        return "gt=" + gt +
                "&id=" + id +
                "&pageKey=" + page_key;
    }
    public FgBelogRequest(String id) {
        this.id = id;
    }


}
