package com.gameservice.outcome.constant;

public interface EnumConstants {

    enum RUMMY_TYPE {
        POINTS, DEALS, POOL_101, POOL_201
    }

    enum DROP_TYPE {
        NONE, FIRST, MIDDLE, AUTO_MIDDLE
    }

    enum DROPPED_BY {
        USER, SYSTEM
    }

    enum SPLIT_TYPE {
        NONE, AUTO, MANUAL
    }

    enum GAME_SUB_TYPE {
        CARDS_10, CARDS_13
    }

    enum GAME_TYPE {
        RUMMY, POKER
    }

    enum PLAYER_STATUS {
        ACTIVE, SPECTATOR, LEFT
    }

    enum CARDS_OWNER {
        PLAYER, TABLE
    }

    enum OUTCOME_TYPE {
        GAME, DEAL
    }

    enum TOURNAMENT_TYPE {
        TIME_BASED, ROUND_BASED
    }

    enum TABLE_TYPE {
        PRACTICE, CASH
    }

    enum GAME_STATUS {
        WAITING, RUNNING, FINISHED
    }

    enum ROOM_TYPE {
        VIP, GAME, TOURNAMENT
    }

    enum DECLARATION_TYPE {
        NONE, WRONG, RIGHT
    }

    enum PICKED_FROM {
        OPEN_DECK, CLOSED_DECK
    }

    enum DECK_TYPE {
        NONE, CLOSED, OPEN
    }

}
