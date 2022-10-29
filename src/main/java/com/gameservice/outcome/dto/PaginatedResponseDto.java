package com.gameservice.outcome.dto;

import lombok.Data;

@Data
public class PaginatedResponseDto<T> extends PageSuccessResponseDto<T> {

    /**
     * Pagination details.
     */
    private PageInfoDto page;

    /**
     * Construct from Page Info.
     *
     * @param data list of order entities.
     * @param page Page Info.
     */
    public PaginatedResponseDto(T data, PageInfoDto page) {
        super(data);
        this.page = page;
    }

}