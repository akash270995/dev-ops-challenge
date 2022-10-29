package com.gameservice.outcome.controller;

import com.gameservice.outcome.constant.ResponseCode;
import com.gameservice.outcome.constant.ResponseMessage;
import com.gameservice.outcome.dto.*;
import com.gameservice.outcome.exception.CustomException;
import com.gameservice.outcome.service.TableService;
import com.gameservice.outcome.util.CommonWebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class TableController {

    @Autowired
    private TableService tableService;

    /**
     * @param tableCreateDto
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @POST /table : API to create table
     */
    @PostMapping("/table")
    public ResponseEntity<?> createTable(@RequestBody TableCreateDto tableCreateDto) {
        log.info("REST request to create table with TableRequest : {}", tableCreateDto);
        TableDetailsDto tableDetailsDto;
        try {
            tableDetailsDto = tableService.createTable(tableCreateDto);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.CREATE_TABLE_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.CREATE_TABLE_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(tableDetailsDto, ResponseMessage.TABLE_CREATE_SUCCESS);
    }

    /**
     * @param id
     * @param userId
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @GET /table/{id} : API to get table details
     */
    @GetMapping("/table/{id}")
    public ResponseEntity<?> getTableDetails(@PathVariable long id, @RequestParam("userId") Long userId) {
        log.info("REST request to get table details with tableId : {}", id);
        TableDetailsDto tableDetailsDto;
        try {
            tableDetailsDto = tableService.getTableDetails(id, userId);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.GET_TABLE_DETAILS_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.GET_TABLE_DETAILS_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(tableDetailsDto);
    }

    /**
     * @param pageRequestDto
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @GET /table/list : API to get table details
     */
    @GetMapping("/table/list")
    public ResponseEntity<?> getTablesList(PageRequestDto pageRequestDto) {
        log.info("REST request to get all tables");
        PaginatedResponseDto<?> tableDetailsDto;
        try {
            tableDetailsDto = tableService.getTablesList(pageRequestDto);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.GET_TABLE_DETAILS_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.GET_TABLE_DETAILS_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(tableDetailsDto);
    }

    /**
     * @param gameId
     * @param dealId
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @GET /table/outcome/{gameId} : API to get table outcome
     */
    @GetMapping("/table/outcome/{gameId}")
    public ResponseEntity<?> getTableOutcome(@PathVariable Long gameId, @RequestParam Long dealId) {
        log.info("REST request to get table outcome with gameId : {} and dealId : {}", gameId, dealId);
        TableOutcomeDto tableOutcomeDto;
        try {
            tableOutcomeDto = tableService.getTableOutcome(gameId, dealId);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.TABLE_OUTCOME_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.TABLE_OUTCOME_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(tableOutcomeDto);
    }

    /**
     * @param gameId
     * @param userId
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @GET /table/leave/{gameId} : API to leave table
     */
    @GetMapping("/table/leave/{gameId}")
    public ResponseEntity<?> leaveTable(@PathVariable Long gameId, @RequestParam Long userId) {
        log.info("REST request to leave table with gameId : {} and userId : {}", gameId, userId);
        try {
            tableService.leaveTable(gameId, userId);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.TABLE_LEAVE_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.TABLE_LEAVE_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(ResponseMessage.TABLE_LEAVE_SUCCESS);
    }

    /**
     * @param gameId
     * @param dealId
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @GET /table/rejoin/{gameId} : API to rejoin table
     */
    @GetMapping("/table/rejoin/{gameId}")
    public ResponseEntity<?> rejoinTable(@PathVariable Long gameId, @RequestParam Long dealId, @RequestParam Long userId) {
        log.info("REST request to rejoin table with gameId : {} , dealId : {} and userId : {}", gameId, dealId, userId);
        try {
            tableService.rejoinTable(gameId, dealId, userId);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.TABLE_REJOIN_ERROR_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.TABLE_REJOIN_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(ResponseMessage.TABLE_REJOIN_SUCCESS);
    }

}
