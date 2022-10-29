package com.gameservice.turn.service.impl;

import com.gameservice.distribution.util.CardGenerationUtils;
import com.gameservice.turn.constant.EnumConstants;
import com.gameservice.turn.dto.PlayersTossCardsDto;
import com.gameservice.turn.dto.TossConfigDto;
import com.gameservice.turn.dto.PlayersTurnDto;
import com.gameservice.turn.model.TurnModel;
import com.gameservice.turn.repository.TurnRepository;
import com.gameservice.turn.service.TossService;
import com.gameservice.turn.util.RummyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Transactional
@Service
public class TossServiceImpl implements TossService {

    @Autowired
    private CardGenerationUtils cardGenerationUtils;

    @Autowired
    private RummyUtils rummyUtils;

    @Autowired
    private TurnRepository turnRepository;

    @Override
    public List<PlayersTurnDto> makeToss(TossConfigDto tossConfigDto) {

        //Return toss if game already exist
        List<TurnModel> turnObj = turnRepository.findByGameId(tossConfigDto.getGameId());
        List<PlayersTurnDto> playersTurnDtos = new ArrayList<>();
        if (turnObj != null && !turnObj.isEmpty()) {
            turnObj.forEach(x -> {
                PlayersTurnDto playersTurnDto = new PlayersTurnDto();
                playersTurnDto.setUserId(x.getUserId());
                playersTurnDto.setTurnOrder(x.getTurnOrder());
                playersTurnDtos.add(playersTurnDto);
            });
            return playersTurnDtos;
        }

        //Make toss if first round
        List<Integer> tossCards = cardGenerationUtils.generateTossCards(tossConfigDto.getPlayerIds().size(), tossConfigDto.getTotalDecks());
        List<PlayersTossCardsDto> playersTossCardsDtos = new ArrayList<>();
        for (int i = 0; i < tossConfigDto.getPlayerIds().size(); i++) {
            PlayersTossCardsDto playersTossCardsDto = new PlayersTossCardsDto();
            playersTossCardsDto.setPlayerId(tossConfigDto.getPlayerIds().get(i));
            playersTossCardsDto.setCard(tossCards.get(i));
            playersTossCardsDto.setCardValue(rummyUtils.getCardValue(tossCards.get(i)));
            playersTossCardsDto.setSuits(rummyUtils.getSuit(tossCards.get(i)));
            playersTossCardsDtos.add(playersTossCardsDto);
        }
        playersTossCardsDtos.sort(Comparator.comparingInt(PlayersTossCardsDto::getCardValue).reversed());
        List<TurnModel> turnModels = new ArrayList<>();
        for (int i = 0; i < playersTossCardsDtos.size(); i++) {
            if ((i != playersTossCardsDtos.size() - 1) && isReplacePos(playersTossCardsDtos.get(i), playersTossCardsDtos.get(i + 1))) {
                // Swapping on basis of suits preferences
                PlayersTossCardsDto current = playersTossCardsDtos.get(i);
                PlayersTossCardsDto next = playersTossCardsDtos.get(i + 1);
                playersTossCardsDtos.set(i, next);
                playersTossCardsDtos.set(i + 1, current);
            }
            TurnModel turnModel = new TurnModel();
            turnModel.setGameId(tossConfigDto.getGameId());
            turnModel.setUserId(playersTossCardsDtos.get(i).getPlayerId());
            turnModel.setTurnOrder(i + 1);
            turnModels.add(turnModel);
            PlayersTurnDto playersTurnDto = new PlayersTurnDto();
            playersTurnDto.setUserId(playersTossCardsDtos.get(i).getPlayerId());
            playersTurnDto.setTurnOrder(i + 1);
            playersTurnDtos.add(playersTurnDto);
        }
        turnRepository.saveAll(turnModels);
        return playersTurnDtos;
    }

    private boolean isReplacePos(PlayersTossCardsDto current, PlayersTossCardsDto next) {
        if (!current.getCardValue().equals(next.getCardValue())) {
            return false;
        } else return (next.getSuits().equals(EnumConstants.SUITS.SPADE)) ||
                (next.getSuits().equals(EnumConstants.SUITS.HEART) && !current.getSuits().equals(EnumConstants.SUITS.SPADE)) ||
                (next.getSuits().equals(EnumConstants.SUITS.DIAMOND) && !current.getSuits().equals(EnumConstants.SUITS.HEART) && !current.getSuits().equals(EnumConstants.SUITS.SPADE));
    }
}
