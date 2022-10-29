package com.gameservice.outcome.service;

import com.gameservice.outcome.dto.RoomCreateDto;
import com.gameservice.outcome.dto.RoomDto;

public interface RoomService {

    RoomDto createRoom(RoomCreateDto roomCreateDto);

    RoomDto getRoomDetails(Long id);

}
