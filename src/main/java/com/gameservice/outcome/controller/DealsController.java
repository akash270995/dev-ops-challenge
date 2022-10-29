package com.gameservice.outcome.controller;

import com.gameservice.outcome.constant.ResponseCode;
import com.gameservice.outcome.constant.ResponseMessage;
import com.gameservice.outcome.dto.DealsDetailsDto;
import com.gameservice.outcome.dto.DealsSaveDto;
import com.gameservice.outcome.exception.CustomException;
import com.gameservice.outcome.service.DealsService;
import com.gameservice.outcome.util.CommonWebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class DealsController {

    @Autowired
    private DealsService dealsService;
/*
    *//**
     * @param dealsSaveDto
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @POST /deals : API to save deal
     *//*
    @PostMapping("/deals")
    public ResponseEntity<?> saveDeal(@RequestBody DealsSaveDto dealsSaveDto) {
        log.info("REST request to save deal with DealRequest : {}", dealsSaveDto);
        DealsDetailsDto dealsDetailsDto;
        try {
            dealsDetailsDto = dealsService.saveDeal(dealsSaveDto);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.SAVE_DEAL_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.SAVE_DEAL_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(dealsDetailsDto, ResponseMessage.DEALS_SAVE_SUCCESS);
    }*/

    /**
     * @param id
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @GET /deals{id} : API to get deal details
     */
    @GetMapping("/deals/{id}")
    public ResponseEntity<?> getDealDetails(@PathVariable long id) {
        log.info("REST request to get deal details with dealId : {}", id);
        DealsDetailsDto dealsDetailsDto;
        try {
            dealsDetailsDto = dealsService.getDealDetails(id);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.GET_DEAL_DETAILS_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.GET_DEAL_DETAILS_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(dealsDetailsDto);
    }


}
