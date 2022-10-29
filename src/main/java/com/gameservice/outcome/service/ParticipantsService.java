package com.gameservice.outcome.service;

import com.gameservice.outcome.dto.ParticipantsDetailsDto;
import com.gameservice.outcome.dto.ParticipantsSaveDto;

import java.util.List;

public interface ParticipantsService {

    ParticipantsDetailsDto joinGame(ParticipantsSaveDto participantsSaveDto);

    List<ParticipantsDetailsDto> getParticipantsDetails(Long gameId);

}
