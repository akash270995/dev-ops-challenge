package com.gameservice.outcome.service;

import com.gameservice.outcome.dto.*;

public interface TableService {

    TableDetailsDto createTable(TableCreateDto tableCreateDto);

    TableDetailsDto getTableDetails(Long id, Long userId);

    PaginatedResponseDto<?> getTablesList(PageRequestDto pageRequestDto);

    TableOutcomeDto getTableOutcome(Long gameId, Long dealId);

    void leaveTable(Long gameId, Long userId);

    void rejoinTable(Long gameId, Long dealId, Long userId);

}
