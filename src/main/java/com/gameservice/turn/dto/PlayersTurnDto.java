package com.gameservice.turn.dto;

import lombok.Data;

@Data
public class PlayersTurnDto {

    private Long userId;
    private Integer turnOrder;

}
