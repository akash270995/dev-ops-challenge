package com.gameservice.outcome.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RoomDto {

    @JsonProperty("roomId")
    private Long id;

    private String roomName;

}
