package com.gameservice.outcome.dto;

import com.gameservice.distribution.dto.PlayerCardsDto;
import lombok.Data;

import java.util.List;

@Data
public class GameStartDto {

    private Long dealId;
    private List<PlayerCardsDto> playersDetail;
    private List<Integer> closedDeckCards;
    private Integer wildJoker;

}
