package com.gameservice.outcome.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gameservice.outcome.constant.EnumConstants;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantsDetailsDto {

    @JsonProperty("participantId")
    private Long id;
    private Long tableId;
    private Long gameId;
    private Long userId;
    private Boolean connected;
    private EnumConstants.PLAYER_STATUS playerStatus;
    private Boolean isSelf;

}
