package com.gameservice.outcome.service.impl;

import com.gameservice.distribution.dto.CardsDistributedDto;
import com.gameservice.distribution.dto.CardsDistributionConfigDto;
import com.gameservice.distribution.dto.ParticipantsDto;
import com.gameservice.distribution.dto.PlayerCardsDto;
import com.gameservice.distribution.service.CardDistributionService;
import com.gameservice.outcome.constant.EnumConstants;
import com.gameservice.outcome.constant.ResponseMessage;
import com.gameservice.outcome.dto.*;
import com.gameservice.outcome.exception.CustomException;
import com.gameservice.outcome.model.GameModel;
import com.gameservice.outcome.model.ParticipantsModel;
import com.gameservice.outcome.model.TableModel;
import com.gameservice.outcome.repository.GameRepository;
import com.gameservice.outcome.repository.ParticipantsRepository;
import com.gameservice.outcome.repository.TableRepository;
import com.gameservice.outcome.service.GameService;
import com.gameservice.turn.constant.CommonConstant;
import com.gameservice.turn.dto.PlayersTurnDto;
import com.gameservice.turn.dto.TossConfigDto;
import com.gameservice.turn.service.TossService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private ParticipantsRepository participantsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CardDistributionService cardDistributionService;

    @Autowired
    private TossService tossService;

    @Override
    public GameDetailsDto createGame(GameCreateDto gameCreateDto) {
        GameModel gameModel = modelMapper.map(gameCreateDto, GameModel.class);
        Optional<TableModel> tableModel = tableRepository.findById(gameModel.getTableId());
        if (!tableModel.isPresent()) throw new CustomException(ResponseMessage.TABLE_NOT_EXIST);
        gameModel.setCurrentDeal(0);
        gameModel.setTotalWager(0L);
        gameModel.setMaxRejoin(tableModel.get().getMaxRejoin());
        gameModel.setTotalDeal(tableModel.get().getTotalDeals());
        gameModel.setStatus(EnumConstants.GAME_STATUS.WAITING);
        GameModel savedGameModel = gameRepository.save(gameModel);
        return modelMapper.map(savedGameModel, GameDetailsDto.class);
    }

    @Override
    public GameDetailsDto getGameDetails(Long id) {
        Optional<GameModel> gameObj = gameRepository.findById(id);
        if (gameObj.isPresent()) {
            return modelMapper.map(gameObj.get(), GameDetailsDto.class);
        } else {
            throw new CustomException(ResponseMessage.GAME_FETCH_ERROR);
        }
    }

    @Override
    public GameStartDto startGame(Long gameId) {
        //Verify participants
        Optional<GameModel> gameModel = gameRepository.findById(gameId);
        if (!gameModel.isPresent()) throw new CustomException(ResponseMessage.GAME_FETCH_ERROR);
        GameModel gameObj = gameModel.get();
        if (gameObj.getCurrentDeal().equals(gameObj.getTotalDeal())) {
            throw new CustomException(ResponseMessage.GAME_START_DEAL_COMPLETED_ERROR);
        } else if (gameObj.getStatus() == EnumConstants.GAME_STATUS.FINISHED) {
            throw new CustomException(ResponseMessage.GAME_FINISHED_ERROR);
        }
        List<ParticipantsModel> participantsModels = participantsRepository.findByGameId(gameId, EnumConstants.PLAYER_STATUS.LEFT);
        TableModel tableModel = gameObj.getTableModel();
        if (participantsModels.size() < tableModel.getMinPlayer() || participantsModels.size() > tableModel.getMaxPlayer())
            throw new CustomException(ResponseMessage.GAME_PLAYER_ERROR);

        //Distribute cards
        List<Long> playerIds = new ArrayList<>();
        List<ParticipantsDto> participantsDtos = new ArrayList<>();
        participantsModels.forEach(x -> {
            playerIds.add(x.getUserId());
            ParticipantsDto participantsDto = new ParticipantsDto();
            participantsDto.setParticipantId(x.getId());
            participantsDto.setPlayerId(x.getUserId());
            participantsDtos.add(participantsDto);
        });
        CardsDistributionConfigDto cardsDistributionConfigDto = new CardsDistributionConfigDto();
        cardsDistributionConfigDto.setGameId(gameId);
        cardsDistributionConfigDto.setCardsType(tableModel.getGameSubType());
        cardsDistributionConfigDto.setParticipants(participantsDtos);
        cardsDistributionConfigDto.setTotalDecks(tableModel.getDeck());
        CardsDistributedDto cardsDistributedDto = cardDistributionService.distributeCards(cardsDistributionConfigDto);

        //Make toss
        TossConfigDto tossConfigDto = new TossConfigDto();
        tossConfigDto.setGameId(gameId);
        tossConfigDto.setPlayerIds(playerIds);
        tossConfigDto.setTotalDecks(CommonConstant.TOSS_DECK);
        List<PlayersTurnDto> playersTurnDtos = tossService.makeToss(tossConfigDto);

        //Update game status
        gameObj.setCurrentDeal(gameObj.getCurrentDeal() + 1);
        gameObj.setStatus(EnumConstants.GAME_STATUS.RUNNING);
        gameRepository.save(gameObj);

        List<PlayerCardsDto> playerCardsDtos = new ArrayList<>();
        Map<Long, PlayerCardsDto> cardsDistributedMap = new HashMap<>();
        Map<Long, PlayersTurnDto> playersTurnMap = new HashMap<>();
        for (int i = 0; i < cardsDistributedDto.getPlayersCards().size(); i++) {
            cardsDistributedMap.put(cardsDistributedDto.getPlayersCards().get(i).getPlayerId(), cardsDistributedDto.getPlayersCards().get(i));
            playersTurnMap.put(playersTurnDtos.get(i).getUserId(), playersTurnDtos.get(i));
        }
        cardsDistributedMap.forEach((k, v) -> {
            PlayerCardsDto playerCardsDto = new PlayerCardsDto();
            playerCardsDto.setPlayerId(k);
            playerCardsDto.setHands(cardsDistributedMap.get(k).getHands());
            playerCardsDto.setParticipantId(cardsDistributedMap.get(k).getParticipantId());
            playerCardsDto.setTurnOrder(playersTurnMap.get(k).getTurnOrder());
            playerCardsDtos.add(playerCardsDto);
        });
        GameStartDto gameStartDto = new GameStartDto();
        gameStartDto.setDealId(cardsDistributedDto.getDealId());
        gameStartDto.setClosedDeckCards(cardsDistributedDto.getClosedDeckCards());
        gameStartDto.setWildJoker(cardsDistributedDto.getWildJoker());
        gameStartDto.setPlayersDetail(playerCardsDtos);
        return gameStartDto;
    }

}
