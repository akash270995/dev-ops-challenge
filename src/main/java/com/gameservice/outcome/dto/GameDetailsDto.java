package com.gameservice.outcome.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gameservice.outcome.constant.EnumConstants;
import lombok.Data;

@Data
public class GameDetailsDto {

    @JsonProperty("gameId")
    private Long id;
    private Long tableId;
    private EnumConstants.GAME_STATUS status;
    private Long currentDeal;
    private Long totalDeal;

}
