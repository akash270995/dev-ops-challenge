package com.gameservice.outcome.util;

import com.gameservice.outcome.constant.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CommonWebUtils {

    /**
     * Method to return success response
     *
     * @param message
     * @param code
     * @return ResponseEntity with SuccessResponse and HttpStatus OK
     */
    public static ResponseEntity<?> successResponse(String message, int code) {
        ResponseData responseData = new ResponseData();
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setSuccess(true);
        responseStatus.setMessage(message);
        responseStatus.setErrorCode(code);
        responseStatus.setHttpCode(HttpStatus.OK.value());
        responseData.setStatus(responseStatus);
        responseData.setBody(new EmptyJsonBody());
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    /**
     * Method to return success response
     *
     * @param data
     * @return ResponseEntity with SuccessResponse and HttpStatus OK
     */
    public static ResponseEntity<?> successResponse(Object data) {
        ResponseData responseData = new ResponseData();
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setSuccess(true);
        responseStatus.setMessage("");
        responseStatus.setErrorCode(ResponseCode.SUCCESS_RESPONSE_CODE);
        responseStatus.setHttpCode(HttpStatus.OK.value());
        responseData.setStatus(responseStatus);
        responseData.setBody(data);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    /**
     * Method to return success response
     *
     * @param data
     * @return ResponseEntity with SuccessResponse and HttpStatus OK
     */
    public static ResponseEntity<?> successResponse(Object data, String message) {
        ResponseData responseData = new ResponseData();
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setSuccess(true);
        responseStatus.setMessage(message);
        responseStatus.setErrorCode(ResponseCode.SUCCESS_RESPONSE_CODE);
        responseStatus.setHttpCode(HttpStatus.OK.value());
        responseData.setStatus(responseStatus);
        responseData.setBody(data);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    /**
     * Method to return failure response
     *
     * @param message
     * @param code
     * @return ResponseEntity with SuccessResponse and HttpStatus BAD_REQUEST
     */
    public static ResponseEntity<?> failureResponse(String message, int code) {
        ResponseData responseData = new ResponseData();
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setSuccess(false);
        responseStatus.setMessage(message);
        responseStatus.setErrorCode(code);
        responseStatus.setHttpCode(HttpStatus.BAD_REQUEST.value());
        responseData.setStatus(responseStatus);
        responseData.setBody(new EmptyJsonBody());
        return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
    }
}
