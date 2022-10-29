package com.gameservice.outcome.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class RummyGenerateOutcomeDto {

    private Long playerId;
    private Boolean winner;
    private Integer gameScore;
    private Integer totalScore = 0;
    @JsonIgnore
    private Integer chipsWon;
    private Boolean eliminated;
    private Boolean canRejoin;
    private List<Integer> pureSequences;
    private List<Integer> impureSequences;
    private List<Integer> sets;
    private List<Integer> invalidCards;
    @JsonIgnore
    private Integer availableDrops;
    private PlayerPrizeDto prize;

}
