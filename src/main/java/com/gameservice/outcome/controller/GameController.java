package com.gameservice.outcome.controller;

import com.gameservice.outcome.constant.EnumConstants;
import com.gameservice.outcome.constant.ResponseCode;
import com.gameservice.outcome.constant.ResponseMessage;
import com.gameservice.outcome.dto.GameCreateDto;
import com.gameservice.outcome.dto.GameDetailsDto;
import com.gameservice.outcome.dto.GameStartDto;
import com.gameservice.outcome.exception.CustomException;
import com.gameservice.outcome.model.HandsModel;
import com.gameservice.outcome.repository.HandsRepository;
import com.gameservice.outcome.service.GameService;
import com.gameservice.outcome.util.CommonWebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private HandsRepository handsRepository;

    /**
     * @param gameCreateDto
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @POST /game : API to create game
     */
    @PostMapping("/game")
    public ResponseEntity<?> createGame(@RequestBody GameCreateDto gameCreateDto) {
        log.info("REST request to create game with GameRequest : {}", gameCreateDto);
        GameDetailsDto gameDetailsDto;
        try {
            gameDetailsDto = gameService.createGame(gameCreateDto);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.CREATE_GAME_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.CREATE_GAME_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(gameDetailsDto, ResponseMessage.GAME_CREATE_SUCCESS);
    }

    /**
     * @param id
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @GET /game : API to get game details
     */
    @GetMapping("/game/{id}")
    public ResponseEntity<?> getGameDetails(@PathVariable long id) {
        log.info("REST request to get game details with gameId : {}", id);
        GameDetailsDto gameDetailsDto;
        try {
            gameDetailsDto = gameService.getGameDetails(id);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.GET_GAME_DETAILS_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.GET_GAME_DETAILS_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(gameDetailsDto);
    }

    /**
     * @param gameId
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @GET /game : API to start game
     */
    @GetMapping("/game/start/{gameId}")
    public ResponseEntity<?> startGame(@PathVariable long gameId) {
        log.info("REST request to start game with gameId : {}", gameId);
        GameStartDto gameStartDto;
        try {
            gameStartDto = gameService.startGame(gameId);
        } catch (CustomException customException) {
            return CommonWebUtils.failureResponse(customException.getMessage(), ResponseCode.START_GAME_ERROR_CODE);
        } catch (Exception e) {
            return CommonWebUtils.failureResponse(ResponseMessage.INTERNAL_SERVER_ERROR, ResponseCode.START_GAME_ERROR_CODE);
        }
        return CommonWebUtils.successResponse(gameStartDto);
    }



    /**
     * @param
     * @return CommonWebUtils with failure response
     * @return CommonWebUtils with success response
     * @GET /game : API to start game
     */
    @GetMapping("/game/dummy/hands/{dealId}")
    public ResponseEntity<?> dummy(@PathVariable long dealId, @RequestParam long participantOneId,  @RequestParam long participantTwoId) {
        List<HandsModel> handsModels = new ArrayList<>();


        //Example 1
        //Player 1
        HandsModel handsModel = new HandsModel();
        List<Integer> cards = new ArrayList<>();
        handsModel.setHands(new ArrayList<>());
        handsModel.setParticipantId(participantOneId);
        cards.add(5);
        cards.add(6);
        cards.add(7);
        cards.add(32);
        cards.add(33);
        cards.add(34);
        handsModel.setPureSequences(cards);
        cards = new ArrayList<>();
        cards.add(22);
        cards.add(23);
        cards.add(24);
        handsModel.setImpureSequences(cards);
        cards = new ArrayList<>();
        cards.add(5);
        cards.add(18);
        cards.add(31);
        cards.add(44);
        handsModel.setSets(cards);
        handsModel.setInvalidCards(new ArrayList<>());
        handsModel.setDropType(EnumConstants.DROP_TYPE.NONE);
        handsModel.setDealId(dealId);
        handsModel.setCardsOwner(EnumConstants.CARDS_OWNER.PLAYER);
        handsModels.add(handsModel);

//Player 2
        handsModel = new HandsModel();
        cards = new ArrayList<>();
        handsModel.setHands(new ArrayList<>());
        handsModel.setParticipantId(participantTwoId);
        handsModel.setPureSequences(new ArrayList<>());
        handsModel.setImpureSequences(new ArrayList<>());
        handsModel.setSets(new ArrayList<>());
        cards.add(19);
        cards.add(25);
        cards.add(26);
        cards.add(28);
        cards.add(30);
        cards.add(35);
        cards.add(58);
        cards.add(38);
        cards.add(48);
        cards.add(49);
        cards.add(59);
        cards.add(62);
        cards.add(64);
        handsModel.setInvalidCards(cards);
        handsModel.setDropType(EnumConstants.DROP_TYPE.NONE);
        handsModel.setDealId(dealId);
        handsModel.setCardsOwner(EnumConstants.CARDS_OWNER.PLAYER);

/*

        //Example 2
        //Player 1
        HandsModel handsModel = new HandsModel();
        List<Integer> cards = new ArrayList<>();
        handsModel.setHands(new ArrayList<>());
        handsModel.setParticipantId(participantOneId);
        cards.add(5);
        cards.add(6);
        cards.add(7);
        cards.add(32);
        cards.add(33);
        cards.add(34);
        handsModel.setPureSequences(cards);
        cards = new ArrayList<>();
        cards.add(22);
        cards.add(23);
        cards.add(24);
        handsModel.setImpureSequences(cards);
        cards = new ArrayList<>();
        cards.add(5);
        cards.add(18);
        cards.add(31);
        cards.add(44);
        handsModel.setSets(cards);
        handsModel.setInvalidCards(new ArrayList<>());
        handsModel.setDropType(EnumConstants.DROP_TYPE.NONE);
        handsModel.setDealId(dealId);
        handsModel.setCardsOwner(EnumConstants.CARDS_OWNER.PLAYER);
        handsModels.add(handsModel);

//Player 2
        handsModel = new HandsModel();
        cards = new ArrayList<>();
        handsModel.setHands(new ArrayList<>());
        handsModel.setParticipantId(participantTwoId);
        cards.add(35);
        cards.add(36);
        cards.add(37);
        handsModel.setPureSequences(cards);
        cards = new ArrayList<>();
        cards.add(62);
        cards.add(64);
        cards.add(49);
        handsModel.setImpureSequences(cards);
        cards = new ArrayList<>();
        handsModel.setInvalidCards(new ArrayList<>());
        handsModel.setSets(new ArrayList<>());
        cards.add(19);
        cards.add(25);
        cards.add(26);
        cards.add(28);
        cards.add(30);
        cards.add(48);
        cards.add(59);
        handsModel.setInvalidCards(cards);
        handsModel.setDropType(EnumConstants.DROP_TYPE.NONE);
        handsModel.setDealId(dealId);
        handsModel.setCardsOwner(EnumConstants.CARDS_OWNER.PLAYER);
*/




        handsModels.add(handsModel);
        handsRepository.saveAll(handsModels);
        return CommonWebUtils.successResponse("saved");
    }

}
