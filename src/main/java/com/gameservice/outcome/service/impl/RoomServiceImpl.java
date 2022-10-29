package com.gameservice.outcome.service.impl;

import com.gameservice.outcome.constant.ResponseMessage;
import com.gameservice.outcome.dto.RoomCreateDto;
import com.gameservice.outcome.dto.RoomDto;
import com.gameservice.outcome.exception.CustomException;
import com.gameservice.outcome.model.RoomModel;
import com.gameservice.outcome.repository.RoomRepository;
import com.gameservice.outcome.service.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RoomDto createRoom(RoomCreateDto roomCreateDto) {
        RoomModel roomModel = modelMapper.map(roomCreateDto, RoomModel.class);
        roomRepository.save(roomModel);
        return modelMapper.map(roomModel, RoomDto.class);
    }

    @Override
    public RoomDto getRoomDetails(Long id) {
        Optional<RoomModel> roomObj = roomRepository.findById(id);
        if (roomObj.isPresent()) {
            RoomModel roomModel = roomObj.get();
            return modelMapper.map(roomModel, RoomDto.class);
        }else {
            throw new CustomException(ResponseMessage.ROOM_FETCH_ERROR);
        }
    }

}
