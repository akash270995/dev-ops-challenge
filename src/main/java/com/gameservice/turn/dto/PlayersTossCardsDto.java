package com.gameservice.turn.dto;

import com.gameservice.turn.constant.EnumConstants;
import lombok.Data;

@Data
public class PlayersTossCardsDto {

    private Long playerId;
    private Integer card;
    private Integer cardValue;
    private EnumConstants.SUITS suits;

}
