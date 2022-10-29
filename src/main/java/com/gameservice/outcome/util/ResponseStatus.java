package com.gameservice.outcome.util;

import lombok.Data;

@Data
public class ResponseStatus {

    private boolean success;
    private int httpCode;
    private int errorCode;
    private String message;

}
