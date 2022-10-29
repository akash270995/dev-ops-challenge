package com.gameservice.turn.service.impl;

import com.gameservice.outcome.constant.EnumConstants;
import com.gameservice.outcome.exception.CustomException;
import com.gameservice.outcome.model.HandsModel;
import com.gameservice.outcome.model.ParticipantsModel;
import com.gameservice.outcome.repository.HandsRepository;
import com.gameservice.outcome.repository.ParticipantsRepository;
import com.gameservice.turn.constant.CommonConstant;
import com.gameservice.turn.constant.ResponseMessage;
import com.gameservice.turn.dto.TurnDetailsDto;
import com.gameservice.turn.dto.TurnDetailsSaveDto;
import com.gameservice.turn.model.TurnModel;
import com.gameservice.turn.repository.TurnRepository;
import com.gameservice.turn.service.TurnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TurnServiceImpl implements TurnService {

    @Autowired
    private TurnRepository turnRepository;

    @Autowired
    private ParticipantsRepository participantsRepository;

    @Autowired
    private HandsRepository handsRepository;

    @Override
    public TurnDetailsDto getTurnDetails(long gameId, long dealId, long userId) {
        List<TurnModel> turnModel = turnRepository.findByGameId(gameId);
        HandsModel lastHand = getHands(handsRepository.getLastHand(dealId, gameId, EnumConstants.CARDS_OWNER.PLAYER, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"))));
        Optional<TurnModel> activeTurnPlayer = turnModel.stream().filter(x -> x.getTurnStatus() == com.gameservice.turn.constant.EnumConstants.TURN_STATUS.ACTIVE).findAny();
        TurnModel turnPlayer;
        if (!activeTurnPlayer.isPresent()) {
            turnPlayer = playerTurn(turnModel, lastHand);
            // Remove all dropped / eliminated / wrong declaration players from turn model
            for (; ; ) {
                HandsModel playerLastHand = getHands(handsRepository.getLastHandByPlayerId(dealId, gameId, EnumConstants.CARDS_OWNER.PLAYER, turnPlayer.getUserId(), PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"))));
                if (playerLastHand == null || (playerLastHand.getDropType() == EnumConstants.DROP_TYPE.NONE && playerLastHand.getParticipantsModel() != null && !playerLastHand.getParticipantsModel().getEliminated() && playerLastHand.getDeclarationType() != null && playerLastHand.getDeclarationType() == EnumConstants.DECLARATION_TYPE.NONE)) {
                    break;
                } else {
                    TurnModel finalTurnPlayer = turnPlayer;
                    turnModel.removeIf(x -> x.getUserId().equals(finalTurnPlayer.getUserId()));
                    turnPlayer = playerTurn(turnModel, lastHand);
                }
            }
            LocalDateTime turnStartTime = LocalDateTime.now();
            turnPlayer.setTurnTime(turnStartTime);
            turnPlayer.setTurnStatus(com.gameservice.turn.constant.EnumConstants.TURN_STATUS.ACTIVE);
            turnRepository.save(turnPlayer);
        } else {
            turnPlayer = activeTurnPlayer.get();
        }
        // Closed deck cards
        HandsModel closedDeckCards = getHands(handsRepository.getTableDeck(dealId, EnumConstants.CARDS_OWNER.TABLE, EnumConstants.DECK_TYPE.CLOSED, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"))));
        // Open deck cards
        HandsModel openDeckCards = getHands(handsRepository.getTableDeck(dealId, EnumConstants.CARDS_OWNER.TABLE, EnumConstants.DECK_TYPE.OPEN, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"))));
        ParticipantsModel participantsModel = participantsRepository.findByGameIdAndUserId(gameId, turnPlayer.getUserId(), EnumConstants.PLAYER_STATUS.LEFT);
        if (participantsModel == null) throw new CustomException(ResponseMessage.PARTICIPANT_NOT_FOUND_ERROR);
        TurnDetailsDto turnDetailsDto = new TurnDetailsDto();
        turnDetailsDto.setConnected(participantsModel.getConnected());
        turnDetailsDto.setCurrentTurnUserId(turnPlayer.getUserId());
        turnDetailsDto.setTurnOrder(turnDetailsDto.getTurnOrder());
        turnDetailsDto.setHasDeclared(lastHand.getDeclarationType() == EnumConstants.DECLARATION_TYPE.RIGHT);
        turnDetailsDto.setCurrentUserTurn(turnPlayer.getUserId() == userId);
        turnDetailsDto.setTurnStartTime(turnPlayer.getTurnTime());
        turnDetailsDto.setClosedDeck(closedDeckCards.getHands());
        turnDetailsDto.setOpenDeck(openDeckCards != null ? openDeckCards.getHands() : null);
        turnDetailsDto.setTurnTimer(CommonConstant.TURN_TIMER);
        turnDetailsDto.setExtraTurnTimer(CommonConstant.EXTRA_TURN_TIMER);
        turnDetailsDto.setDisconnectedTurnTimer(CommonConstant.DISCONNECTED_TURN_TIMER);
        turnDetailsDto.setMeldingTimer(CommonConstant.MELDING_TIMER);
        return turnDetailsDto;
    }

    @Override
    public void saveTurnDetails(long gameId, long dealId, TurnDetailsSaveDto turnDetailsSaveDto) {
        ParticipantsModel participantsModel = participantsRepository.findByGameIdAndUserId(gameId, turnDetailsSaveDto.getUserId(), EnumConstants.PLAYER_STATUS.LEFT);
        if (participantsModel == null) throw new CustomException(ResponseMessage.PARTICIPANT_NOT_FOUND_ERROR);
        HandsModel lastHand = getHands(handsRepository.getLastHandByPlayerId(dealId, gameId, EnumConstants.CARDS_OWNER.PLAYER, turnDetailsSaveDto.getUserId(), PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"))));
        HandsModel handsModel = new HandsModel();
        handsModel.setDealId(dealId);
        handsModel.setCardsOwner(EnumConstants.CARDS_OWNER.PLAYER);
        handsModel.setParticipantId(participantsModel.getId());
        handsModel.setCurrentDisconnectedTurn(lastHand == null || participantsModel.getConnected() ? 0 : lastHand.getCurrentDisconnectedTurn() + 1);
        handsModel.setCurrentTurnNumber(lastHand != null && lastHand.getCurrentTurnNumber() != null ? lastHand.getCurrentTurnNumber() + 1 : 1);
        // User drop
        if (turnDetailsSaveDto.getDropped() != null && turnDetailsSaveDto.getDropped()) {
            handsModel.setDropType(lastHand != null ? EnumConstants.DROP_TYPE.MIDDLE : EnumConstants.DROP_TYPE.FIRST);
            handsModel.setDroppedBy(EnumConstants.DROPPED_BY.USER);
            handsRepository.save(handsModel);
            return;
        }
        // Consecutive miss drop
        if (turnDetailsSaveDto.getMissTurnsDrop() != null && handsModel.getCurrentDisconnectedTurn().equals(turnDetailsSaveDto.getMissTurnsDrop())) {
            handsModel.setDropType(lastHand != null ? EnumConstants.DROP_TYPE.MIDDLE : EnumConstants.DROP_TYPE.FIRST);
            handsModel.setDroppedBy(EnumConstants.DROPPED_BY.SYSTEM);
        }
        // 21 disconnected turns
        if (handsModel.getCurrentDisconnectedTurn() == CommonConstant.TURN_DISCONNECTED_DROP) {
            handsModel.setDropType(EnumConstants.DROP_TYPE.AUTO_MIDDLE);
            handsModel.setDroppedBy(EnumConstants.DROPPED_BY.SYSTEM);
        }
        // First drop by system
        if (turnDetailsSaveDto.getMissTurnsDrop() == null && lastHand == null && turnDetailsSaveDto.getPlayerInactive()) {
            handsModel.setDropType(EnumConstants.DROP_TYPE.FIRST);
            handsModel.setDroppedBy(EnumConstants.DROPPED_BY.SYSTEM);
        }
        participantsModel.setConnected(!turnDetailsSaveDto.getPlayerInactive());
        participantsRepository.save(participantsModel);
        TurnModel turnModel = turnRepository.findByGameIdAndUserId(gameId, participantsModel.getUserId());
        turnModel.setTurnStatus(com.gameservice.turn.constant.EnumConstants.TURN_STATUS.INACTIVE);
        turnRepository.save(turnModel);
        handsModel.setHands(turnDetailsSaveDto.getHands());
        handsModel.setPureSequences(turnDetailsSaveDto.getPureSequences());
        handsModel.setImpureSequences(turnDetailsSaveDto.getImpureSequences());
        handsModel.setSets(turnDetailsSaveDto.getSets());
        handsModel.setInvalidCards(turnDetailsSaveDto.getInvalidCards());
        handsModel.setCardDropped(turnDetailsSaveDto.getCardDropped());
        handsModel.setCardPicked(turnDetailsSaveDto.getCardPicked());
        handsModel.setDisconnected(!participantsModel.getConnected());
        handsModel.setTurnStartTime(turnModel.getTurnTime());
        handsModel.setTurnEndTime(LocalDateTime.now());
        handsModel.setPickedFrom(turnDetailsSaveDto.getPickedFrom());
        if (turnDetailsSaveDto.getHasDeclared() && turnDetailsSaveDto.getPureSequences().size() > 0 && (turnDetailsSaveDto.getPureSequences().size() > 5 || turnDetailsSaveDto.getImpureSequences().size() > 0) && turnDetailsSaveDto.getInvalidCards().size() > 0) {
            handsModel.setDeclarationType(EnumConstants.DECLARATION_TYPE.RIGHT);
        } else if (turnDetailsSaveDto.getHasDeclared()) {
            handsModel.setDeclarationType(EnumConstants.DECLARATION_TYPE.WRONG);
        }
        handsRepository.save(handsModel);
        if (turnDetailsSaveDto.getPickedFrom() == EnumConstants.PICKED_FROM.CLOSED_DECK) {
            // Save Closed Deck
            handsModel = new HandsModel();
            HandsModel closedDeckCards = getHands(handsRepository.getTableDeck(dealId, EnumConstants.CARDS_OWNER.TABLE, EnumConstants.DECK_TYPE.CLOSED, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"))));
            List<Integer> hands = closedDeckCards.getHands();
            hands.removeIf(x -> x.equals(turnDetailsSaveDto.getCardPicked()));
            handsModel.setHands(hands);
            handsModel.setDealId(dealId);
            handsModel.setCardsOwner(EnumConstants.CARDS_OWNER.TABLE);
            handsRepository.save(handsModel);
        } else if (turnDetailsSaveDto.getPickedFrom() == EnumConstants.PICKED_FROM.OPEN_DECK) {
            // Save Open Deck
            handsModel = new HandsModel();
            HandsModel openDeckCards = getHands(handsRepository.getTableDeck(dealId, EnumConstants.CARDS_OWNER.TABLE, EnumConstants.DECK_TYPE.OPEN, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"))));
            List<Integer> hands = openDeckCards != null ? openDeckCards.getHands() : new ArrayList<>();
            hands.removeIf(x -> x.equals(turnDetailsSaveDto.getCardPicked()));
            handsModel.setHands(hands);
            handsModel.setDealId(dealId);
            handsModel.setCardsOwner(EnumConstants.CARDS_OWNER.TABLE);
            handsRepository.save(handsModel);
        }
        // Save dropped card to db
        handsModel = new HandsModel();
        HandsModel openDeckCards = getHands(handsRepository.getTableDeck(dealId, EnumConstants.CARDS_OWNER.TABLE, EnumConstants.DECK_TYPE.OPEN, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id"))));
        List<Integer> hands = openDeckCards != null ? openDeckCards.getHands() : new ArrayList<>();
        hands.add(turnDetailsSaveDto.getCardDropped());
        handsModel.setHands(hands);
        handsModel.setDealId(dealId);
        handsModel.setCardsOwner(EnumConstants.CARDS_OWNER.TABLE);
        handsRepository.save(handsModel);
    }

    private TurnModel playerTurn(List<TurnModel> turnModel, HandsModel lastHand) {
        TurnModel turnPlayer = null;
        for (int i = 0; i < turnModel.size(); i++) {
            if (lastHand == null || i == turnModel.size() - 1) {
                turnPlayer = turnModel.get(0);
                break;
            }
            if (turnModel.get(i).getUserId().equals(lastHand.getParticipantsModel().getUserId())) {
                turnPlayer = turnModel.get(i + 1);
                break;
            }
        }
        return turnPlayer;
    }

    private HandsModel getHands(List<HandsModel> handsModels) {
        return handsModels != null && !handsModels.isEmpty() && handsModels.get(0) != null ? handsModels.get(0) : null;
    }
}
