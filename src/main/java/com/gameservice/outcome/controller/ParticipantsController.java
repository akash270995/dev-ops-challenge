package com.gameservice.outcome.controller;

import com.gameservice.outcome.constant.ResponseCode;
import com.gameservice.outcome.constant.ResponseMessage;
import com.gameservice.outcome.dto.ParticipantsDetailsDto;
import com.gameservice.outcome.dto.ParticipantsSaveDto;
import com.gameservice.outcome.exception.CustomException;
import com.gameservice.outcome.service.ParticipantsService;
import com.gameservice.outcome.util.CommonWebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ParticipantsController {

    @Autowired
    private ParticipantsService participantsService;

    /**
     * @param participantsSaveDto
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @POST /participants : API to save participants
     */
    @PostMapping("/participants")
    public ResponseEntity<?> joinGame(@RequestBody ParticipantsSaveDto participantsSaveDto) {
        log.info("REST request to save participants with ParticipantsRequest : {}", participantsSaveDto);
        ParticipantsDetailsDto participantsDetailsDto;
        try {
            participantsDetailsDto = participantsService.joinGame(participantsSaveDto);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.SAVE_PARTICIPANTS_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.SAVE_PARTICIPANTS_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(participantsDetailsDto, ResponseMessage.PARTICIPANTS_SAVE_SUCCESS);
    }

    /**
     * @param gameId
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @GET /participants{gameId} : API to get participants details
     */
    @GetMapping("/participants/{gameId}")
    public ResponseEntity<?> getParticipantsDetails(@PathVariable long gameId) {
        log.info("REST request to get participants details with gameId : {}", gameId);
        List<ParticipantsDetailsDto> participantsDetailsDto;
        try {
            participantsDetailsDto = participantsService.getParticipantsDetails(gameId);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.GET_PARTICIPANTS_DETAILS_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.GET_PARTICIPANTS_DETAILS_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(participantsDetailsDto);
    }


}
