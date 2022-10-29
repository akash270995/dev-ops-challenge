package com.gameservice.distribution.dto;

import lombok.Data;

import java.util.List;

@Data
public class CardsDistributedDto {

    private Long dealId;
    private List<PlayerCardsDto> playersCards;
    private List<Integer> closedDeckCards;
    private Integer wildJoker;

}
