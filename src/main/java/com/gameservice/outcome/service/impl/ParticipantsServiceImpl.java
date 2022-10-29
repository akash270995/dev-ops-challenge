package com.gameservice.outcome.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gameservice.outcome.constant.CommonConstant;
import com.gameservice.outcome.constant.EnumConstants;
import com.gameservice.outcome.constant.ResponseMessage;
import com.gameservice.outcome.dto.BalanceDto;
import com.gameservice.outcome.dto.GameCreateDto;
import com.gameservice.outcome.dto.ParticipantsDetailsDto;
import com.gameservice.outcome.dto.ParticipantsSaveDto;
import com.gameservice.outcome.exception.CustomException;
import com.gameservice.outcome.model.GameModel;
import com.gameservice.outcome.model.ParticipantsModel;
import com.gameservice.outcome.model.TableModel;
import com.gameservice.outcome.repository.GameRepository;
import com.gameservice.outcome.repository.ParticipantsRepository;
import com.gameservice.outcome.repository.TableRepository;
import com.gameservice.outcome.service.GameService;
import com.gameservice.outcome.service.ParticipantsService;
import com.gameservice.outcome.util.CashierServiceUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantsServiceImpl implements ParticipantsService {

    @Autowired
    private ParticipantsRepository participantsRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CashierServiceUtils cashierServiceUtils;

    @Autowired
    private GameService gameService;

    @Override
    public ParticipantsDetailsDto joinGame(ParticipantsSaveDto participantsSaveDto) {
        Long gameId;
        // Fetch Game Id for auto spawn
        List<GameModel> gameModels = gameRepository.gameForAutoSpawn(participantsSaveDto.getTableId(), EnumConstants.GAME_STATUS.WAITING, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "id")));
        if (gameModels != null && !gameModels.isEmpty()) {
            gameId = gameModels.get(0).getId();
            Long totalParticipants = participantsRepository.findTotalPlayersOnTable(gameId);
            Long totalTablePlayers = tableRepository.findTotalTablePlayers(participantsSaveDto.getTableId());
            // Create new game if table is full
            if (totalParticipants.equals(totalTablePlayers)) {
                GameCreateDto gameCreateDto = new GameCreateDto();
                gameCreateDto.setTableId(participantsSaveDto.getTableId());
                gameId = gameService.createGame(gameCreateDto).getId();
            }
        } else {
            GameCreateDto gameCreateDto = new GameCreateDto();
            gameCreateDto.setTableId(participantsSaveDto.getTableId());
            gameId = gameService.createGame(gameCreateDto).getId();
        }
        Optional<GameModel> gameModel = gameRepository.findById(gameId);
        if (!gameModel.isPresent()) throw new CustomException(ResponseMessage.GAME_FETCH_ERROR);
        GameModel gameObj = gameModel.get();
        Optional<TableModel> tableModel = tableRepository.findById(gameObj.getTableId());
        if (!tableModel.isPresent()) throw new CustomException(ResponseMessage.TABLE_NOT_EXIST_ERROR);
        TableModel tableObj = tableModel.get();
        participantsSaveDto.setConnected(true);
        participantsSaveDto.setPlayerStatus(EnumConstants.PLAYER_STATUS.ACTIVE);
        if (gameObj.getStatus() == EnumConstants.GAME_STATUS.RUNNING) {
            if (tableObj.getGameType() == EnumConstants.RUMMY_TYPE.POINTS) {
                participantsSaveDto.setPlayerStatus(EnumConstants.PLAYER_STATUS.SPECTATOR);
            } else {
                throw new CustomException(ResponseMessage.PARTICIPANTS_JOIN_ERROR);
            }
        } else if (gameObj.getStatus() == EnumConstants.GAME_STATUS.FINISHED) {
            throw new CustomException(ResponseMessage.GAME_FINISHED_ERROR);
        }
        //Participant already joined game
        ParticipantsModel participantExist = participantsRepository.findByGameIdAndUserId(gameId, participantsSaveDto.getUserId(), EnumConstants.PLAYER_STATUS.LEFT);
        if (participantExist != null) throw new CustomException(ResponseMessage.PARTICIPANT_ALREADY_JOINED);
        //Verify wager amount
        BalanceDto balanceDto = cashierServiceUtils.getBalance(participantsSaveDto.getUserId());
        BalanceDto deductBalance = new BalanceDto();
        deductBalance.setUserId(participantsSaveDto.getUserId());
        deductBalance.setGameId(gameObj.getId());
        if (tableObj.getGameType() == EnumConstants.RUMMY_TYPE.POINTS) {
            Long minBalance = (long) (tableObj.getPointsValue() * CommonConstant.RUMMY_MAX_CAPPED_POINTS);
            if (balanceDto.getCashBalance() < minBalance) {
                throw new CustomException(ResponseMessage.INSUFFICIENT_BALANCE);
            }
            deductBalance.setCashBalance(-minBalance);
        } else if (balanceDto.getCashBalance() < tableObj.getMinBuyin()) {
            throw new CustomException(ResponseMessage.INSUFFICIENT_BALANCE);
        } else {
            // Transaction type is wager to make transaction entry for deals and pools
            deductBalance.setTransactionType(CommonConstant.TRANSACTION_TYPE_GAME_WAGER);
            deductBalance.setCashBalance(-((long) tableObj.getMinBuyin()));
        }
        //Update cashier service balance
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String requestBody = objectWriter.writeValueAsString(deductBalance);
            cashierServiceUtils.updateBalance(requestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //Update game total wager
        gameObj.setTotalWager(gameObj.getTotalWager() + (tableObj.getGameType() == EnumConstants.RUMMY_TYPE.POINTS ? 0 : (-deductBalance.getCashBalance())));
        gameRepository.save(gameObj);
        //Join player
        ParticipantsModel participantsModel = modelMapper.map(participantsSaveDto, ParticipantsModel.class);
        participantsModel.setGameId(gameId);
        participantsModel.setSubWalletBalance(-deductBalance.getCashBalance());
        participantsModel.setTopupWalletBalance(0L);
        participantsModel.setWager(tableObj.getGameType() == EnumConstants.RUMMY_TYPE.POINTS ? 0 : -deductBalance.getCashBalance());
        ParticipantsModel savedParticipantsModel = participantsRepository.save(participantsModel);
        ParticipantsDetailsDto participantsDetailsDto = modelMapper.map(savedParticipantsModel, ParticipantsDetailsDto.class);
        participantsDetailsDto.setTableId(participantsSaveDto.getTableId());
        return participantsDetailsDto;
    }

    @Override
    public List<ParticipantsDetailsDto> getParticipantsDetails(Long gameId) {
        List<ParticipantsModel> participantsModels = participantsRepository.findByGameId(gameId, EnumConstants.PLAYER_STATUS.LEFT);
        if (participantsModels != null) {
            return modelMapper.map(participantsModels, new TypeToken<List<ParticipantsDetailsDto>>() {
            }.getType());
        } else {
            throw new CustomException(ResponseMessage.PARTICIPANTS_FETCH_ERROR);
        }
    }

}
