package com.gameservice.outcome.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gameservice.outcome.constant.EnumConstants;
import lombok.Data;

@Data
public class PlayerPrizeDto {

    @JsonIgnore
    private long playerId;
    private long prizeAmount;
    @JsonIgnore
    private long totalRake;
    private EnumConstants.SPLIT_TYPE splitType = EnumConstants.SPLIT_TYPE.NONE;

}
