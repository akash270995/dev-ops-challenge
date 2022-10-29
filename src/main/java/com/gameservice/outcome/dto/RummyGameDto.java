package com.gameservice.outcome.dto;

import com.gameservice.outcome.constant.EnumConstants;
import lombok.Data;

import java.util.List;

@Data
public class RummyGameDto {

    private int wildJoker;
    private float pointValue;
    private EnumConstants.RUMMY_TYPE rummyType;
    private List<PlayerCardsDto> playerCardsDto;
    private List<RummyPreviousDealOutcomeDto> rummyPreviousDealOutcomeDtos;

}
