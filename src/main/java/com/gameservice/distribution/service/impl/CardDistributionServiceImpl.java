package com.gameservice.distribution.service.impl;

import com.gameservice.distribution.dto.CardsDistributedDto;
import com.gameservice.distribution.dto.CardsDistributionConfigDto;
import com.gameservice.distribution.dto.CardsGeneratedDto;
import com.gameservice.distribution.dto.PlayerCardsDto;
import com.gameservice.distribution.service.CardDistributionService;
import com.gameservice.distribution.util.CardGenerationUtils;
import com.gameservice.outcome.constant.EnumConstants;
import com.gameservice.outcome.dto.DealsDetailsDto;
import com.gameservice.outcome.dto.DealsSaveDto;
import com.gameservice.outcome.model.HandsModel;
import com.gameservice.outcome.repository.HandsRepository;
import com.gameservice.outcome.service.DealsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardDistributionServiceImpl implements CardDistributionService {

    @Autowired
    private CardGenerationUtils cardGenerationUtils;

    @Autowired
    private HandsRepository handsRepository;

    @Autowired
    private DealsService dealsService;

    @Override
    public CardsDistributedDto distributeCards(CardsDistributionConfigDto cardsDistributionConfigDto) {
        CardsGeneratedDto cardsGeneratedDto = cardGenerationUtils.getDistributionCards(cardsDistributionConfigDto.getParticipants().size(), cardsDistributionConfigDto.getTotalDecks(), cardsDistributionConfigDto.getCardsType());
        List<HandsModel> handsModels = new ArrayList<>();
        List<PlayerCardsDto> playerCardsDtos = new ArrayList<>();
        //Save deal
        DealsSaveDto dealsSaveDto = new DealsSaveDto();
        dealsSaveDto.setGameId(cardsDistributionConfigDto.getGameId());
        dealsSaveDto.setWildJoker(cardsGeneratedDto.getWildJoker());
        DealsDetailsDto dealsDetailsDto = dealsService.saveDeal(dealsSaveDto);
        // To save players distributed cards
        for (int i = 0; i < cardsDistributionConfigDto.getParticipants().size(); i++) {
            HandsModel handsModel = new HandsModel();
            handsModel.setHands(cardsGeneratedDto.getPlayersCards().get(i));
            handsModel.setParticipantId(cardsDistributionConfigDto.getParticipants().get(i).getParticipantId());
            handsModel.setDealId(dealsDetailsDto.getId());
            handsModel.setCardsOwner(EnumConstants.CARDS_OWNER.PLAYER);
            handsModels.add(handsModel);
            // To return cards from function
            PlayerCardsDto playerCardsDto = new PlayerCardsDto();
            playerCardsDto.setParticipantId(cardsDistributionConfigDto.getParticipants().get(i).getParticipantId());
            playerCardsDto.setPlayerId(cardsDistributionConfigDto.getParticipants().get(i).getPlayerId());
            playerCardsDto.setHands(cardsGeneratedDto.getPlayersCards().get(i));
            playerCardsDtos.add(playerCardsDto);
        }

        // To save closed deck cards
        HandsModel handsModel = new HandsModel();
        handsModel.setHands(cardsGeneratedDto.getClosedDeckCards());
        handsModel.setDealId(dealsDetailsDto.getId());
        handsModel.setDeckType(EnumConstants.DECK_TYPE.CLOSED);
        handsModel.setCardsOwner(EnumConstants.CARDS_OWNER.TABLE);
        handsModels.add(handsModel);
        handsRepository.saveAll(handsModels);
        CardsDistributedDto cardsDistributedDto = new CardsDistributedDto();
        cardsDistributedDto.setDealId(dealsDetailsDto.getId());
        cardsDistributedDto.setWildJoker(dealsSaveDto.getWildJoker());
        cardsDistributedDto.setPlayersCards(playerCardsDtos);
        cardsDistributedDto.setClosedDeckCards(cardsGeneratedDto.getClosedDeckCards());
        return cardsDistributedDto;
    }
}
