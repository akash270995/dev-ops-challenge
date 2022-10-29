package com.gameservice.turn.dto;

import com.gameservice.outcome.constant.EnumConstants;
import lombok.Data;

import java.util.List;

@Data
public class TurnDetailsSaveDto {

    private Long userId;
    private List<Integer> hands;
    private List<Integer> sets;
    private List<Integer> pureSequences;
    private List<Integer> impureSequences;
    private List<Integer> invalidCards;
    private Integer cardDropped;
    private Integer cardPicked;
    private Boolean dropped;
    private Boolean hasDeclared;
    private Boolean playerInactive;
    private EnumConstants.PICKED_FROM pickedFrom;
    private Integer missTurnsDrop; // Player is dropped on missing this number of turns

}
