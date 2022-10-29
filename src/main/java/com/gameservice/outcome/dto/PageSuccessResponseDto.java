package com.gameservice.outcome.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageSuccessResponseDto<T> {

    /**
     * Response data.
     */
    protected T data;

}
