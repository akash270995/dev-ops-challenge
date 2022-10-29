package com.gameservice.outcome.controller;

import com.gameservice.outcome.constant.ResponseCode;
import com.gameservice.outcome.constant.ResponseMessage;
import com.gameservice.outcome.dto.PageRequestDto;
import com.gameservice.outcome.exception.CustomException;
import com.gameservice.outcome.service.ReportService;
import com.gameservice.outcome.util.CommonWebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * @param pageRequestDto
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @GET /report/total/rake : API to get total rake
     */
    @GetMapping("/report/total/rake")
    public ResponseEntity<?> getTotalRake(PageRequestDto pageRequestDto) {
        log.info("REST request to get total rake : {}", pageRequestDto);
        Long totalRake;
        try {
            totalRake = reportService.getTotalRake(pageRequestDto);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.GET_TOTAL_RAKE_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.GET_TOTAL_RAKE_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(totalRake);
    }

}
