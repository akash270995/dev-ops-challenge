package com.gameservice.outcome.dto;

import com.gameservice.outcome.constant.EnumConstants;
import lombok.Data;

import java.util.List;

@Data
public class PlayerCardsDto {

    private long playerId;
    private List<Integer> pureSequences;
    private List<Integer> impureSequences;
    private List<Integer> sets;
    private List<Integer> invalidCards;
    private EnumConstants.DROP_TYPE dropType;
    private EnumConstants.DECLARATION_TYPE declarationType;

}
