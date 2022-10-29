package com.gameservice.outcome.dto;

import com.gameservice.outcome.constant.EnumConstants;
import lombok.Data;

@Data
public class TableCreateDto {

    private Long roomId;
    private EnumConstants.RUMMY_TYPE gameType;
    private EnumConstants.GAME_SUB_TYPE gameSubType;
    private Integer minPlayer;
    private Integer maxPlayer;
    private Integer minBuyin;
    private Integer maxBuyin;
    private Integer deck;
    private Integer rake;
    private Integer tableOrder;
    private EnumConstants.TABLE_TYPE type;
    private Integer totalDeals;
    private EnumConstants.GAME_TYPE game;
    private Boolean active;
    private Float pointsValue;
    private Integer maxRejoin;

}
