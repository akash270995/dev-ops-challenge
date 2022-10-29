package com.gameservice.tournament.service;

import com.gameservice.outcome.dto.PageRequestDto;
import com.gameservice.outcome.dto.PaginatedResponseDto;
import com.gameservice.tournament.dto.TournamentCreateDto;
import com.gameservice.tournament.dto.TournamentDetailsDto;

public interface TournamentService {

    TournamentDetailsDto createTournament(TournamentCreateDto tournamentCreateDto);

    TournamentDetailsDto getTournamentDetails(Long id, Long userId);

    PaginatedResponseDto<?> getTournamentsList(PageRequestDto pageRequestDto);

}
