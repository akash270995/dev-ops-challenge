package com.gameservice.outcome.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gameservice.outcome.constant.CommonConstant;
import com.gameservice.outcome.constant.EnumConstants;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class PageRequestDto {

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
    @JsonIgnore
    private Long totalRecords;

    /**
     * Pagination offset.
     */
    @JsonIgnore
    private Integer offset;

    /**
     * Pagination limit.
     */
    @JsonIgnore
    private Integer limit;

    /**
     * Sort parameter.
     */
    private String sort;

    /**
     * Sorting order.
     */
    private String sortOrder;

    /**
     * Game type.
     */
    private String gameType;

    /**
     * Game sub type.
     */
    private String gameSubType;

    /**
     * Table type.
     */
    private String tableType;

    /**
     * Minimum player.
     */
    private Integer minPlayer;

    /**
     * Maximum player.
     */
    private Integer maxPlayer;

    /**
     * Maximum player.
     */
    private Float pointsValue;

    /**
     * Minimum buyin.
     */
    private Integer minBuyin;

    /**
     * Maximum buyin.
     */
    private Integer maxBuyin;

    /**
     * From Date (YYYY-MM-DD) or (YYYY-MM-DD-HH-MM-SS)
     */
    private String fromDate;

    /**
     * To Date (YYYY-MM-DD) or (YYYY-MM-DD-HH-MM-SS)
     */
    private String toDate;

    /**
     * Construct from page,pageSize,sortBy,sortOrder.
     *
     * @param page      Page Number.
     * @param pageSize  Records per page.
     * @param sortBy    Sort parameter.
     * @param sortOrder Sorting order.
     */
    public PageRequestDto(@RequestParam(name = CommonConstant.QUERY_PARAM_PAGE, required = false) Integer page,
                          @RequestParam(name = CommonConstant.QUERY_PARAM_PAGE_SIZE, required = false) Integer pageSize,
                          @RequestParam(name = CommonConstant.QUERY_PARAM_SORT_BY, required = false) String sortBy,
                          @RequestParam(name = CommonConstant.QUERY_PARAM_SORT_ORDER, required = false) String sortOrder,
                          @RequestParam(name = CommonConstant.QUERY_PARAM_GAME_TYPE, required = false) String gameType,
                          @RequestParam(name = CommonConstant.QUERY_PARAM_GAME_SUB_TYPE, required = false) String gameSubType,
                          @RequestParam(name = CommonConstant.QUERY_PARAM_TABLE_TYPE, required = false) String tableType,
                          @RequestParam(name = CommonConstant.QUERY_PARAM_MIN_BUYIN, required = false) Integer minBuyin,
                          @RequestParam(name = CommonConstant.QUERY_PARAM_MAX_BUYIN, required = false) Integer maxBuyin,
                          @RequestParam(name = CommonConstant.QUERY_PARAM_FROM_DATE, required = false) String fromDate,
                          @RequestParam(name = CommonConstant.QUERY_PARAM_TO_DATE, required = false) String toDate) {

        this.page = (null != page && page > 0) ? page : 1;
        this.pageSize = null == pageSize ? CommonConstant.PAGE_SIZE_DEFAULT : pageSize;
        this.pageSize = (this.pageSize < 1) ? CommonConstant.PAGE_SIZE_DEFAULT : this.pageSize;
        this.pageSize = (this.pageSize > CommonConstant.PAGE_SIZE_MAX) ? CommonConstant.PAGE_SIZE_MAX : this.pageSize;

        this.sort = sortBy;

        this.sortOrder = (null != sortOrder && (sortOrder.equals(CommonConstant.SORT_ORDER_ASCENDING)
                || sortOrder.equals(CommonConstant.SORT_ORDER_DESCENDING))) ? sortOrder
                : CommonConstant.SORT_ORDER_ASCENDING;
        this.offset = (this.page == 1) ? 0 : ((page - 1) * this.pageSize);
        this.limit = this.pageSize;
        this.gameType = gameType;
        this.gameSubType = gameSubType;
        this.tableType = tableType;
        this.minBuyin = minBuyin;
        this.maxBuyin = maxBuyin;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

}
