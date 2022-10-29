package com.gameservice.distribution.dto;

import lombok.Data;

import java.util.List;

@Data
public class CardsGeneratedDto {

    private List<List<Integer>> playersCards;
    private List<Integer> closedDeckCards;
    private Integer wildJoker;

}
