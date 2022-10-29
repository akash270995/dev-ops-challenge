package com.gameservice.outcome.util;

import lombok.Data;

@Data
public class ResponseData {

    private ResponseStatus status;
    private Object body;

}
