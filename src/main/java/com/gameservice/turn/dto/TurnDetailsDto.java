package com.gameservice.turn.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TurnDetailsDto {

    private Long currentTurnUserId;
    private Boolean currentUserTurn;
    private Boolean connected;
    private Integer turnOrder;
    private Boolean hasDeclared;
    private LocalDateTime turnStartTime;
    private Integer turnTimer;
    private Integer extraTurnTimer;
    private Integer disconnectedTurnTimer;
    private Integer meldingTimer;
    private List<Integer> openDeck;
    private List<Integer> closedDeck;

}
