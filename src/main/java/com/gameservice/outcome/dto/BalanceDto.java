package com.gameservice.outcome.dto;

import lombok.Data;

@Data
public class BalanceDto {

    private Long userId;

    private Long gameId;

    private Long cashBalance;

    private Long bonusBalance;

    private Long virtualBalance;

    private Long lockedCashBalance;

    private String expiryDate; //YYYY-MM-DD

    private String transactionType;

    private Long wagerAmount;

}
