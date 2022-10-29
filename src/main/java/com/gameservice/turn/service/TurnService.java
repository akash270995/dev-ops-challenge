package com.gameservice.turn.service;

import com.gameservice.turn.dto.TurnDetailsDto;
import com.gameservice.turn.dto.TurnDetailsSaveDto;

public interface TurnService {

    TurnDetailsDto getTurnDetails(long gameId, long dealId, long userId);

    void saveTurnDetails(long gameId, long dealId, TurnDetailsSaveDto turnDetailsSaveDto);

}
