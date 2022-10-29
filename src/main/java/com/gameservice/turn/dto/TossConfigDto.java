package com.gameservice.turn.dto;

import lombok.Data;

import java.util.List;

@Data
public class TossConfigDto {

    private Long gameId;
    private List<Long> playerIds;
    private Integer totalDecks;

}
