package com.gameservice.turn.controller;

import com.gameservice.outcome.constant.ResponseCode;
import com.gameservice.outcome.constant.ResponseMessage;
import com.gameservice.outcome.exception.CustomException;
import com.gameservice.outcome.util.CommonWebUtils;
import com.gameservice.turn.dto.TurnDetailsDto;
import com.gameservice.turn.dto.TurnDetailsSaveDto;
import com.gameservice.turn.service.TurnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class TurnController {

    @Autowired
    private TurnService turnService;

    /**
     * @param turnDetailsSaveDto
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @POST /table/turn/{gameId} : API to save turn
     */
    @PostMapping("/table/turn/{gameId}")
    public ResponseEntity<?> saveTurn(@PathVariable Long gameId, @RequestParam("dealId") Long dealId, @RequestBody TurnDetailsSaveDto turnDetailsSaveDto) {
        log.info("REST request to save turn with TurnDetailsSaveRequest : {}", turnDetailsSaveDto);
        try {
            turnService.saveTurnDetails(gameId, dealId, turnDetailsSaveDto);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.SAVE_TURN_DETAILS_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.SAVE_TURN_DETAILS_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(ResponseMessage.TURN_DETAILS_SAVE_SUCCESS);
    }

    /**
     * @param gameId
     * @param dealId
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @GET /table/turn/{gameId} : API to get turn details
     */
    @GetMapping("/table/turn/{gameId}")
    public ResponseEntity<?> getTurnDetails(@PathVariable Long gameId, @RequestParam("dealId") Long dealId, @RequestParam("userId") Long userId) {
        log.info("REST request to get turn details with gameId : {}", gameId);
        TurnDetailsDto turnDetailsDto;
        try {
            turnDetailsDto = turnService.getTurnDetails(gameId, dealId, userId);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.GET_TURN_DETAILS_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.GET_TURN_DETAILS_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(turnDetailsDto);
    }

}
