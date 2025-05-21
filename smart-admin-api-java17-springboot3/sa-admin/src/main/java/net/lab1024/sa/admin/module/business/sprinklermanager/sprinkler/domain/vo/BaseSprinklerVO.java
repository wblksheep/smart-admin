package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseSprinklerVO {
    @Schema(description = "历史")
    private String history;
}
