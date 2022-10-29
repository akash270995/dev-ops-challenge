package com.gameservice.outcome.service;

import com.gameservice.outcome.dto.GameCreateDto;
import com.gameservice.outcome.dto.GameDetailsDto;
import com.gameservice.outcome.dto.GameStartDto;

public interface GameService {

    GameDetailsDto createGame(GameCreateDto gameCreateDto);

    GameDetailsDto getGameDetails(Long id);

    GameStartDto startGame(Long gameId);

}
