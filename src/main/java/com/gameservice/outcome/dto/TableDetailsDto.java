package com.gameservice.outcome.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gameservice.outcome.constant.EnumConstants;
import lombok.Data;

import java.util.List;

@Data
public class TableDetailsDto {

    @JsonProperty("tableId")
    private Long id;
    private Long roomId;
    private EnumConstants.RUMMY_TYPE gameType;
    private EnumConstants.GAME_SUB_TYPE gameSubType;
    private Integer minPlayer;
    private Integer maxPlayer;
    private Integer minBuyin;
    private Integer maxBuyin;
    private Integer deck;
    private Integer rake;
    private EnumConstants.GAME_TYPE game;
    private Boolean active;
    private Long waitingPlayers;
    private Long totalPlayers;
    private List<ParticipantsDetailsDto> participants;

}
