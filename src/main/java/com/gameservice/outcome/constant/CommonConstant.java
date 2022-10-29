package com.gameservice.outcome.constant;

public interface CommonConstant {

    int ACE_POINT_VALUE = 10;

    int RUMMY_POOL_101_POINTS = 101;

    int RUMMY_POOL_201_POINTS = 201;

    int RUMMY_POINTS_FIRST_DROP_POINTS = 20;

    int RUMMY_POINTS_MIDDLE_DROP_POINTS = 40;

    int RUMMY_POOL_101_FIRST_DROP_POINTS = 20;

    int RUMMY_POOL_101_MIDDLE_DROP_POINTS = 40;

    int RUMMY_POOL_201_FIRST_DROP_POINTS = 25;

    int RUMMY_POOL_201_MIDDLE_DROP_POINTS = 50;

    int RUMMY_POINTS_AUTO_MIDDLE_DROP_POINTS = 40;

    int RUMMY_POOL_101_AUTO_MIDDLE_DROP_POINTS = 40;

    int RUMMY_POOL_201_AUTO_MIDDLE_DROP_POINTS = 50;

    int RUMMY_MAX_CAPPED_POINTS = 80;

    int RUMMY_POOL_101_REJOIN_POINTS = 79;

    int RUMMY_POOL_201_REJOIN_POINTS = 174;

    int RUMMY_POOL_101_AUTO_SPLIT_POINTS = 80;

    int RUMMY_POOL_201_AUTO_SPLIT_POINTS = 175;

    int RUMMY_POOL_101_MANUAL_SPLIT_POINTS = 60;

    int RUMMY_POOL_201_MANUAL_SPLIT_POINTS = 150;

    int RUMMY_POINTS_FIRST_HAND_SHOW_POINTS = 40;

    int RUMMY_DEALS_FIRST_HAND_SHOW_POINTS = 40;

    int RUMMY_POOL_101_FIRST_HAND_SHOW_POINTS = 40;

    int RUMMY_POOL_201_FIRST_HAND_SHOW_POINTS = 40;

    int RUMMY_WRONG_DECLARATION_POINTS = 80;

    /**
     * Ascending order.
     */
    String SORT_ORDER_ASCENDING = "asc";

    /**
     * Descending order.
     */
    String SORT_ORDER_DESCENDING = "desc";

    /**
     * Maximum API pagination size.
     */
    int PAGE_SIZE_MAX = 100;

    /**
     * Default API pagination size.
     */
    int PAGE_SIZE_DEFAULT = 20;

    /**
     * Page query parameter.
     */
    String QUERY_PARAM_PAGE = "page";

    /**
     * Page size query parameter.
     */
    String QUERY_PARAM_PAGE_SIZE = "pageSize";

    /**
     * Sort by query parameter.
     */
    String QUERY_PARAM_SORT_BY = "sortBy";

    /**
     * Sort order query parameter.
     */
    String QUERY_PARAM_SORT_ORDER = "sortOrder";

    /**
     * Game type query parameter
     */
    String QUERY_PARAM_GAME_TYPE = "gameType";

    /**
     * Game sub type query parameter
     */

    String QUERY_PARAM_GAME_SUB_TYPE = "gameSubType";

    /**
     * Table type query parameter
     */

    String QUERY_PARAM_TABLE_TYPE = "tableType";

    /**
     * Min buyin query parameter
     */

    String QUERY_PARAM_MIN_BUYIN = "minBuyin";

    /**
     * Max buyin query parameter
     */

    String QUERY_PARAM_MAX_BUYIN = "maxBuyin";

    String TRANSACTION_TYPE_GAME_WAGER = "GAME_WAGER";

    String TRANSACTION_TYPE_TOURNAMENT_WAGER = "TOURNAMENT_WAGER";

    String TRANSACTION_TYPE_GAME_WINNINGS = "GAME_WINNINGS";

    String TRANSACTION_TYPE_TOURNAMENT_WINNINGS = "TOURNAMENT_WINNINGS";

    /**
     * From Date query parameter
     */
    String QUERY_PARAM_FROM_DATE = "fromDate";

    /**
     * To Date query parameter
     */
    String QUERY_PARAM_TO_DATE = "toDate";


}
