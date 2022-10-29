package com.gameservice.turn.service;

import com.gameservice.turn.dto.TossConfigDto;
import com.gameservice.turn.dto.PlayersTurnDto;

import java.util.List;

public interface TossService {

    List<PlayersTurnDto> makeToss(TossConfigDto tossConfigDto);

}
