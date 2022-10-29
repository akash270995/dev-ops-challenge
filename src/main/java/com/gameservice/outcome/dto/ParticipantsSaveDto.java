package com.gameservice.outcome.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gameservice.outcome.constant.EnumConstants;
import lombok.Data;

@Data
public class ParticipantsSaveDto {

    private Long tableId;
    private Long userId;
    @JsonIgnore
    private Boolean connected;
    @JsonIgnore
    private EnumConstants.PLAYER_STATUS playerStatus;

}
