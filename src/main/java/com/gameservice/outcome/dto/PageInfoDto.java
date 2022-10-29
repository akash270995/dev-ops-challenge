package com.gameservice.outcome.dto;

import lombok.Data;

@Data
public class PageInfoDto {
    /**
     * Page number.
     */
    private Integer page;
    /**
     * Records per page.
     */
    private Integer pageSize;
    /**
     * total number of records.
     */
    private Long totalRecords;
    /**
     * total page count.
     */
    private Integer totalPage;

    /**
     * Construct from Page Request. * * @param pageRequest Page Request instance.
     */
    public PageInfoDto(PageRequestDto pageRequest) {
        this.page = pageRequest.getPage();
        this.pageSize = pageRequest.getPageSize();
        this.totalRecords = pageRequest.getTotalRecords();
        this.totalPage = (null != pageRequest.getTotalRecords()) ? ((int) Math.ceil((double) pageRequest.getTotalRecords() / (double) pageRequest.getPageSize())) : 0;
    }
}