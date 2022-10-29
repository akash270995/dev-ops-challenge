package com.gameservice.outcome.controller;

import com.gameservice.outcome.constant.ResponseCode;
import com.gameservice.outcome.constant.ResponseMessage;
import com.gameservice.outcome.dto.RoomCreateDto;
import com.gameservice.outcome.dto.RoomDto;
import com.gameservice.outcome.dto.TableDetailsDto;
import com.gameservice.outcome.exception.CustomException;
import com.gameservice.outcome.service.RoomService;
import com.gameservice.outcome.util.CommonWebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    /**
     * @param roomCreateDto
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @POST /room : API to create room
     */
    @PostMapping("/room")
    public ResponseEntity<?> createRoom(@RequestBody RoomCreateDto roomCreateDto) {
        log.info("REST request to create room with RoomRequest : {}", roomCreateDto);
        RoomDto roomDto;
        try {
            roomDto = roomService.createRoom(roomCreateDto);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.CREATE_ROOM_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.CREATE_ROOM_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(roomDto, ResponseMessage.ROOM_CREATE_SUCCESS);
    }

    /**
     * @param id
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @GET /room/{id} : API to get room details
     */
    @GetMapping("/room/{id}")
    public ResponseEntity<?> getRoomDetails(@PathVariable long id) {
        log.info("REST request to get room details with roomId : {}", id);
        RoomDto roomDto;
        try {
            roomDto = roomService.getRoomDetails(id);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.GET_ROOM_DETAILS_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.GET_ROOM_DETAILS_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(roomDto);
    }

}
