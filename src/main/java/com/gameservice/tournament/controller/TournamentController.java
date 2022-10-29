package com.gameservice.tournament.controller;

import com.gameservice.outcome.constant.ResponseCode;
import com.gameservice.outcome.constant.ResponseMessage;
import com.gameservice.outcome.dto.PageRequestDto;
import com.gameservice.outcome.dto.PaginatedResponseDto;
import com.gameservice.outcome.exception.CustomException;
import com.gameservice.outcome.util.CommonWebUtils;
import com.gameservice.tournament.dto.TournamentCreateDto;
import com.gameservice.tournament.dto.TournamentDetailsDto;
import com.gameservice.tournament.service.TournamentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    /**
     * @param tournamentCreateDto
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @POST /tournament : API to create tournament
     */
    @PostMapping("/tournament")
    public ResponseEntity<?> createTournament(@RequestBody TournamentCreateDto tournamentCreateDto) {
        log.info("REST request to create tournament with TournamentRequest : {}", tournamentCreateDto);
        TournamentDetailsDto tournamentDetailsDto;
        try {
            tournamentDetailsDto = tournamentService.createTournament(tournamentCreateDto);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.CREATE_TOURNAMENT_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.CREATE_TOURNAMENT_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(tournamentDetailsDto, ResponseMessage.TOURNAMENT_CREATE_SUCCESS);
    }

    /**
     * @param id
     * @param userId
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @GET /tournament/{id} : API to get tournament details
     */
    @GetMapping("/tournament/{id}")
    public ResponseEntity<?> getTournamentDetails(@PathVariable long id, @RequestParam("userId") Long userId) {
        log.info("REST request to get tournament details with tournamentId : {}", id);
        TournamentDetailsDto tournamentDetailsDto;
        try {
            tournamentDetailsDto = tournamentService.getTournamentDetails(id, userId);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.GET_TOURNAMENT_DETAILS_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.GET_TOURNAMENT_DETAILS_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(tournamentDetailsDto);
    }

    /**
     * @param pageRequestDto
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @GET /tournament/list : API to get tournaments list
     */
    @GetMapping("/tournament/list")
    public ResponseEntity<?> getTournamentsList(PageRequestDto pageRequestDto) {
        log.info("REST request to get all tournaments");
        PaginatedResponseDto<?> tournamentDetailsDto;
        try {
            tournamentDetailsDto = tournamentService.getTournamentsList(pageRequestDto);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.GET_TOURNAMENT_DETAILS_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.GET_TOURNAMENT_DETAILS_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(tournamentDetailsDto);
    }

}
