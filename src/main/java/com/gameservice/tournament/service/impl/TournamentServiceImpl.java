package com.gameservice.tournament.service.impl;

import com.gameservice.outcome.dto.PageRequestDto;
import com.gameservice.outcome.dto.PaginatedResponseDto;
import com.gameservice.tournament.dto.TournamentCreateDto;
import com.gameservice.tournament.dto.TournamentDetailsDto;
import com.gameservice.tournament.service.TournamentService;
import org.springframework.stereotype.Service;

@Service
public class TournamentServiceImpl implements TournamentService {

    @Override
    public TournamentDetailsDto createTournament(TournamentCreateDto tournamentCreateDto) {
        return null;
    }

    @Override
    public TournamentDetailsDto getTournamentDetails(Long id, Long userId) {
        return null;
    }

    @Override
    public PaginatedResponseDto<?> getTournamentsList(PageRequestDto pageRequestDto) {
        return null;
    }
}
