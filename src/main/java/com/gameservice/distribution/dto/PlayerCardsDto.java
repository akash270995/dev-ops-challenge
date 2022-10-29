package com.gameservice.distribution.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlayerCardsDto {

    private Long playerId;
    private Long participantId;
    private Integer turnOrder;
    private List<Integer> hands;

}
