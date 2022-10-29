package com.gameservice.outcome.constant;

public interface ResponseMessage {

    String INTERNAL_SERVER_ERROR = "An internal error occurred.";

    String DEALS_DROP_ERROR = "No drop is available for deals.";

    String ROOM_CREATE_SUCCESS = "Room created successfully.";

    String ROOM_FETCH_ERROR = "Unable to fetch room details.";

    String TABLE_CREATE_SUCCESS = "Table created successfully.";

    String TABLE_FETCH_ERROR = "Unable to fetch table details.";

    String GAME_CREATE_SUCCESS = "Game created successfully.";

    String GAME_FETCH_ERROR = "Unable to fetch game details.";

    String GAME_RUNNING_ERROR = "Game is already running.";

    String GAME_FINISHED_ERROR = "Game is already finished.";

    String GAME_PLAYER_ERROR = "Minimum and maximum players are not valid.";

    String PARTICIPANTS_SAVE_SUCCESS = "Participant saved successfully.";

    String PARTICIPANTS_FETCH_ERROR = "Unable to fetch participants details.";

    String PARTICIPANTS_JOIN_ERROR = "Unable to join participants as game is already running.";

    String DEALS_SAVE_SUCCESS = "Deal saved successfully.";

    String DEALS_FETCH_ERROR = "Unable to fetch deals details.";

    String GAME_ID_INVALID_ERROR = "Invalid game id provided.";

    String TABLE_NOT_EXIST_ERROR = "Table does not exist.";

    String DEAL_NOT_EXIST_ERROR = "Deal does not exist.";

    String CASHIER_SERVICE_FETCH_BALANCE_ERROR = "Unable to fetch cashier service balance.";

    String CASHIER_SERVICE_UPDATE_BALANCE_ERROR = "Unable to update cashier service balance.";

    String OUTCOME_ALREADY_GENERATED = "Outcome is already generated for this deal.";

    String TABLE_NOT_EXIST = "Table does not exist.";

    String INSUFFICIENT_BALANCE = "Insufficient balance to join table.";

    String INSUFFICIENT_BALANCE_REJOIN = "Insufficient balance to rejoin table.";

    String PARTICIPANT_ALREADY_JOINED = "Participant has already joined game.";

    String GAME_START_DEAL_COMPLETED_ERROR = "All deals are completed. Game cannot started.";

    String TURN_DETAILS_SAVE_SUCCESS = "Turn details saved successfully.";

    String TABLE_LEAVE_SUCCESS = "Table left successfully.";

    String REJOIN_GAME_TYPE_ERROR = "Only pool game can be rejoined.";

    String REJOIN_GAME_SCORE_UPDATE_ERROR = "Unable to update rejoin game score.";

    String TABLE_REJOIN_SUCCESS = "Table rejoined successfully.";

    String TOURNAMENT_CREATE_SUCCESS = "Tournament created successfully.";

}
