package com.gameservice.distribution.dto;

import com.gameservice.outcome.constant.EnumConstants;
import lombok.Data;

import java.util.List;

@Data
public class CardsDistributionConfigDto {

    private Long gameId;
    private int totalDecks;
    private EnumConstants.GAME_SUB_TYPE cardsType;
    private List<ParticipantsDto> participants;

}
