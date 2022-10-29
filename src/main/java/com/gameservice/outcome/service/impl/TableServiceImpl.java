package com.gameservice.outcome.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gameservice.outcome.constant.CommonConstant;
import com.gameservice.outcome.constant.EnumConstants;
import com.gameservice.outcome.constant.ResponseMessage;
import com.gameservice.outcome.dto.*;
import com.gameservice.outcome.exception.CustomException;
import com.gameservice.outcome.model.*;
import com.gameservice.outcome.repository.*;
import com.gameservice.outcome.service.TableService;
import com.gameservice.outcome.specifications.builder.SpecificationsBuilder;
import com.gameservice.outcome.util.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Slf4j
@Service
public class TableServiceImpl implements TableService {

    private static final Map<String, String> tableFilters;

    static {
        tableFilters = new HashMap<>();
        tableFilters.put("table_order", "order");
    }

    @Autowired
    RummyOutcomeUtils rummyOutcomeUtils;

    @Autowired
    RummyPrizeUtils rummyPrizeUtils;

    @Autowired
    private HandsRepository handsRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private DealsRepository dealsRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private OutcomeRepository outcomeRepository;

    @Autowired
    private ParticipantsRepository participantsRepository;

    @Autowired
    private RejoinRepository rejoinRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CashierServiceUtils cashierServiceUtils;

    @Override
    public TableDetailsDto createTable(TableCreateDto tableCreateDto) {
        TableModel tableModel = modelMapper.map(tableCreateDto, TableModel.class);
        TableModel savedTableModel = tableRepository.save(tableModel);
        return modelMapper.map(savedTableModel, TableDetailsDto.class);
    }

    @Override
    public TableDetailsDto getTableDetails(Long id, Long userId) {
        Optional<TableModel> tableObj = tableRepository.findById(id);
        if (tableObj.isPresent()) {
            TableModel tableModel = tableObj.get();
            TableDetailsDto tableDetailsDto = modelMapper.map(tableModel, TableDetailsDto.class);
            List<ParticipantsDetailsDto> participantsDetailsDtoList = modelMapper.map(participantsRepository.findByGameId(tableDetailsDto.getId(), EnumConstants.PLAYER_STATUS.LEFT), new TypeToken<List<ParticipantsDetailsDto>>() {
            }.getType());
            participantsDetailsDtoList.stream().filter(x -> x.getUserId().equals(userId)).findAny().ifPresent(x -> x.setIsSelf(true));
            tableDetailsDto.setParticipants(participantsDetailsDtoList);
            return tableDetailsDto;
        } else {
            throw new CustomException(ResponseMessage.TABLE_FETCH_ERROR);
        }
    }

    @Override
    public PaginatedResponseDto<?> getTablesList(PageRequestDto pageRequestDto) {
        Sort sort = Sort.by("tableOrder").ascending();
        if (pageRequestDto.getSort() != null && pageRequestDto.getSortOrder() != null) {
            sort = pageRequestDto.getSortOrder().equalsIgnoreCase(CommonConstant.SORT_ORDER_ASCENDING) ? Sort.by(tableFilters.get(pageRequestDto.getSort())).ascending()
                    : Sort.by(tableFilters.get(pageRequestDto.getSort())).descending();
        }
        SpecificationsBuilder specificationsBuilder = new SpecificationsBuilder();
        if (StringUtils.hasText(pageRequestDto.getGameType())) {
            try {
                specificationsBuilder.with(new SpecSearchCriteria("gameType", SearchOperation.EQUALITY, EnumConstants.RUMMY_TYPE.valueOf(pageRequestDto.getGameType().toUpperCase())));
            } catch (IllegalArgumentException e) {
                log.error("Game type filter not applied. Invalid value provided : {}", pageRequestDto.getGameType());
            }
        }
        if (StringUtils.hasText(pageRequestDto.getGameSubType())) {
            try {
                specificationsBuilder.with(new SpecSearchCriteria("gameSubType", SearchOperation.EQUALITY, EnumConstants.GAME_SUB_TYPE.valueOf(pageRequestDto.getGameSubType().toUpperCase())));
            } catch (IllegalArgumentException e) {
                log.error("Game sub type filter not applied. Invalid value provided : {}", pageRequestDto.getGameSubType());
            }
        }
        if (StringUtils.hasText(pageRequestDto.getTableType())) {
            try {
                specificationsBuilder.with(new SpecSearchCriteria("tableType", SearchOperation.EQUALITY, EnumConstants.TABLE_TYPE.valueOf(pageRequestDto.getTableType().toUpperCase())));
            } catch (IllegalArgumentException e) {
                log.error("Table type filter not applied. Invalid value provided : {}", pageRequestDto.getTableType());
            }
        }
        if (pageRequestDto.getMinBuyin() != null && pageRequestDto.getMaxBuyin() != null) {
            specificationsBuilder.with(new SpecSearchCriteria("minBuyin", SearchOperation.GREATER_THAN_EQUAL, pageRequestDto.getMinBuyin()));
            specificationsBuilder.with(new SpecSearchCriteria("maxBuyin", SearchOperation.LESS_THAN_EQUAL, pageRequestDto.getMaxBuyin()));
        }
        if (pageRequestDto.getMinPlayer() != null && pageRequestDto.getMaxPlayer() != null) {
            specificationsBuilder.with(new SpecSearchCriteria("minPlayer", SearchOperation.GREATER_THAN_EQUAL, pageRequestDto.getMinPlayer()));
            specificationsBuilder.with(new SpecSearchCriteria("maxPlayer", SearchOperation.LESS_THAN_EQUAL, pageRequestDto.getMaxPlayer()));
        }
        if (pageRequestDto.getPointsValue() != null) {
            specificationsBuilder.with(new SpecSearchCriteria("pointsValue", SearchOperation.EQUALITY, pageRequestDto.getPointsValue()));
        }
        PageRequest pageRequest = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getPageSize(), sort);
        Page<TableModel> tableModels = tableRepository.findAll(specificationsBuilder.build(), pageRequest);
        List<TableDetailsDto> tableDetailsDtos = modelMapper.map(tableModels.getContent(), new TypeToken<List<TableDetailsDto>>() {
        }.getType());
        tableDetailsDtos.forEach(x -> {
            x.setTotalPlayers(gameRepository.countPlayersByStatus(x.getId(), EnumConstants.GAME_STATUS.RUNNING));
            x.setWaitingPlayers(gameRepository.countPlayersByStatus(x.getId(), EnumConstants.GAME_STATUS.WAITING));
        });
        pageRequestDto.setTotalRecords(tableModels.getTotalElements());
        return new PaginatedResponseDto<>(tableDetailsDtos, new PageInfoDto(pageRequestDto));
    }

    @Override
    public TableOutcomeDto getTableOutcome(Long gameId, Long dealId) {
        Optional<GameModel> gameModel = gameRepository.findById(gameId);
        if (!gameModel.isPresent()) throw new CustomException(ResponseMessage.GAME_ID_INVALID_ERROR);
        // Game details
        GameModel gameObject = gameModel.get();
        Optional<DealsModel> dealsModel = dealsRepository.findById(dealId);
        if (!dealsModel.isPresent()) throw new CustomException(ResponseMessage.DEAL_NOT_EXIST_ERROR);
        // Deal details
        DealsModel dealObject = dealsModel.get();
        Optional<TableModel> tableModel = tableRepository.findById(gameObject.getTableId());
        if (!tableModel.isPresent()) throw new CustomException(ResponseMessage.TABLE_NOT_EXIST_ERROR);
        List<OutcomeModel> outcomeModels = outcomeRepository.findByDealId(dealId);
        if (!outcomeModels.isEmpty()) throw new CustomException(ResponseMessage.OUTCOME_ALREADY_GENERATED);
        // Players table details
        TableModel tableObject = tableModel.get();
        // Players last hands details
        List<HandsModel> lastHands = handsRepository.getLastHands(dealId, EnumConstants.CARDS_OWNER.PLAYER);
        //Generate Outcome CardGenerationUtils Config Start
        RummyGameDto rummyGameDto = new RummyGameDto();
        rummyGameDto.setRummyType(tableObject.getGameType());
        rummyGameDto.setWildJoker(dealObject.getWildJoker());
        rummyGameDto.setPointValue(tableObject.getPointsValue());
        List<PlayerCardsDto> playerCardsDtos = new ArrayList<>();
        lastHands.forEach(x -> {
            PlayerCardsDto playerCardsDto = new PlayerCardsDto();
            playerCardsDto.setPlayerId(x.getParticipantsModel().getUserId());
            playerCardsDto.setPureSequences(x.getPureSequences());
            playerCardsDto.setImpureSequences(x.getImpureSequences());
            playerCardsDto.setSets(x.getSets());
            playerCardsDto.setInvalidCards(x.getInvalidCards());
            playerCardsDto.setDropType(x.getDropType());
            playerCardsDto.setDeclarationType(x.getDeclarationType());
            playerCardsDtos.add(playerCardsDto);
        });
        // Previous deals score
        if (tableObject.getGameType() != EnumConstants.RUMMY_TYPE.POINTS) {
            List<RummyPreviousDealOutcomeDto> rummyPreviousDealOutcomeDtos = new ArrayList<>();
            List<DealsModel> previousDeal = dealsRepository.getPreviousDeal(gameId, PageRequest.of(1, 1, Sort.by(Sort.Direction.DESC, "id")));
            List<OutcomeModel> previousDealOutcome = previousDeal != null && previousDeal.size() > 0 ? outcomeRepository.findByDealId(previousDeal.get(0).getId()) : new ArrayList<>();
            previousDealOutcome.forEach(x -> {
                RummyPreviousDealOutcomeDto rummyPreviousDealOutcomeDto = new RummyPreviousDealOutcomeDto();
                rummyPreviousDealOutcomeDto.setPlayerId(x.getUserId());
                rummyPreviousDealOutcomeDto.setTotalScore(x.getTotalScore());
                rummyPreviousDealOutcomeDtos.add(rummyPreviousDealOutcomeDto);
            });
            rummyGameDto.setRummyPreviousDealOutcomeDtos(rummyPreviousDealOutcomeDtos);
        }
        rummyGameDto.setPlayerCardsDto(playerCardsDtos);
        RummyOutcomeDto rummyOutcome = rummyOutcomeUtils.getGameScore(rummyGameDto);
        //Generate Outcome CardGenerationUtils Config End

        long nonEliminatedPlayers = rummyOutcome.getScoreBoard().stream().filter(x -> x.getEliminated() == null || !x.getEliminated()).count();
        EnumConstants.OUTCOME_TYPE outcomeType = EnumConstants.OUTCOME_TYPE.DEAL;
        if (tableObject.getGameType() == EnumConstants.RUMMY_TYPE.POINTS ||
                ((tableObject.getGameType() == EnumConstants.RUMMY_TYPE.DEALS) && gameObject.getTotalDeal().equals(gameObject.getCurrentDeal())) ||
                (((tableObject.getGameType() == EnumConstants.RUMMY_TYPE.POOL_101) || (tableObject.getGameType() == EnumConstants.RUMMY_TYPE.POOL_201)) && nonEliminatedPlayers == 1)) {
            outcomeType = EnumConstants.OUTCOME_TYPE.GAME;
        }

        long totalRake = 0;
        if (outcomeType == EnumConstants.OUTCOME_TYPE.GAME) {
            //Prize Calculation
            int losingPlayersPoints = tableObject.getGameType() == EnumConstants.RUMMY_TYPE.POINTS ? rummyOutcome.getScoreBoard().stream()
                    .mapToInt(RummyGenerateOutcomeDto::getGameScore)
                    .sum()
                    : 0;
            for (int i = 0; i <= rummyOutcome.getScoreBoard().size(); i++) {
                RummyGenerateOutcomeDto x = rummyOutcome.getScoreBoard().get(i);
                if (x.getEliminated() != null && x.getEliminated()) {
                    participantsRepository.eliminatePlayer(gameId, x.getPlayerId());
                }
                if (tableObject.getGameType() == EnumConstants.RUMMY_TYPE.POINTS) {
                    //Update points rummy balance in wallet
                    long balance = (long) (tableObject.getPointsValue() * CommonConstant.RUMMY_MAX_CAPPED_POINTS) - (long) (x.getGameScore() * tableObject.getPointsValue());
                    if (balance > 0) {
                        BalanceDto balanceDto = new BalanceDto();
                        balanceDto.setUserId(x.getPlayerId());
                        balanceDto.setGameId(gameId);
                        balanceDto.setCashBalance(balance);
                        balanceDto.setWagerAmount((long) (x.getGameScore() * tableObject.getPointsValue()));
                        balanceDto.setTransactionType(CommonConstant.TRANSACTION_TYPE_GAME_WAGER);
                        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
                        try {
                            String requestBody = objectWriter.writeValueAsString(balanceDto);
                            cashierServiceUtils.updateBalance(requestBody);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if (x.getWinner() != null && x.getWinner()) {
                    BalanceDto balanceDto = new BalanceDto();
                    PlayerPrizeDto playerPrizeDto;
                    if (tableObject.getGameType() == EnumConstants.RUMMY_TYPE.POINTS) {
                        playerPrizeDto = rummyPrizeUtils.calculatePointsPrize(losingPlayersPoints, tableObject.getPointsValue(), tableObject.getRake());
                    } else {
                        playerPrizeDto = rummyPrizeUtils.calculatePoolsAndDealsPrize(gameObject.getTotalWager(), tableObject.getRake());
                    }
                    totalRake = playerPrizeDto.getTotalRake();
                    x.setPrize(playerPrizeDto);
                    //Update winnings
                    balanceDto.setUserId(x.getPlayerId());
                    balanceDto.setGameId(gameId);
                    balanceDto.setCashBalance(playerPrizeDto.getPrizeAmount());
                    balanceDto.setTransactionType(CommonConstant.TRANSACTION_TYPE_GAME_WINNINGS);
                    ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    try {
                        String requestBody = objectWriter.writeValueAsString(balanceDto);
                        cashierServiceUtils.updateBalance(requestBody);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        List<OutcomeModel> outcomeModelsObj = new ArrayList<>();
        EnumConstants.OUTCOME_TYPE finalOutcomeType = outcomeType;
        long totalWager = 0L;
        for (int i = 0; i <= rummyOutcome.getScoreBoard().size(); i++) {
            OutcomeModel outcomeModel = new OutcomeModel();
            RummyGenerateOutcomeDto x = rummyOutcome.getScoreBoard().get(i);
            outcomeModel.setDealId(dealId);
            outcomeModel.setUserId(x.getPlayerId());
            outcomeModel.setWinner(x.getWinner() != null && x.getWinner());
            outcomeModel.setPlayerRank(outcomeModelsObj.size() + 1);
            outcomeModel.setOutcomeType(finalOutcomeType);
            outcomeModel.setGameScore(x.getGameScore());
            outcomeModel.setTotalScore(x.getTotalScore());
            outcomeModel.setWinningAmount(x.getPrize() != null ? x.getPrize().getPrizeAmount() : 0);
            outcomeModelsObj.add(outcomeModel);
            //Update participant wager for points game
            if (tableObject.getGameType() == EnumConstants.RUMMY_TYPE.POINTS) {
                totalWager += (long) (x.getGameScore() * tableObject.getPointsValue());
                participantsRepository.updateParticipantWager(gameId, x.getPlayerId(), (long) (x.getGameScore() * tableObject.getPointsValue()));
            }
        }
        //Update game total wager for points game
        if (tableObject.getGameType() == EnumConstants.RUMMY_TYPE.POINTS) {
            gameObject.setTotalWager(totalWager);
            gameRepository.save(gameObject);
        }

        //Update game total rake
        if (outcomeType == EnumConstants.OUTCOME_TYPE.GAME) {
            gameObject.setTotalRake(totalRake);
            gameRepository.save(gameObject);
        }

        //Save outcome in db
        outcomeRepository.saveAll(outcomeModelsObj);
        TableOutcomeDto tableOutcomeDto = new TableOutcomeDto();
        tableOutcomeDto.setScoreBoard(rummyOutcome.getScoreBoard());
        tableOutcomeDto.setSplitType(rummyOutcome.getSplitType());
        tableOutcomeDto.setOutcomeType(outcomeType);
        return tableOutcomeDto;
    }

    @Override
    public void leaveTable(Long gameId, Long userId) {
        Optional<GameModel> gameModel = gameRepository.findById(gameId);
        if (!gameModel.isPresent()) throw new CustomException(ResponseMessage.GAME_ID_INVALID_ERROR);
        // Game details
        GameModel gameObject = gameModel.get();
        Optional<TableModel> tableModel = tableRepository.findById(gameObject.getTableId());
        if (!tableModel.isPresent()) throw new CustomException(ResponseMessage.TABLE_NOT_EXIST_ERROR);
        // Players table details
        TableModel tableObject = tableModel.get();
        ParticipantsModel participantsModel = participantsRepository.findByGameIdAndUserId(gameId, userId, EnumConstants.PLAYER_STATUS.LEFT);
        if (participantsModel == null) throw new CustomException(ResponseMessage.PARTICIPANTS_FETCH_ERROR);
        // Update game wager
        gameObject.setTotalWager(gameObject.getTotalWager() - (tableObject.getGameType() == EnumConstants.RUMMY_TYPE.POINTS ? 0 : participantsModel.getSubWalletBalance()));
        gameRepository.save(gameObject);
        // Refund wager to player
        BalanceDto refundBalance = new BalanceDto();
        refundBalance.setUserId(userId);
        refundBalance.setTransactionType(CommonConstant.TRANSACTION_TYPE_GAME_WAGER);
        refundBalance.setCashBalance(participantsModel.getSubWalletBalance());
        refundBalance.setGameId(gameId);
        // Update cashier service balance
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String requestBody = objectWriter.writeValueAsString(refundBalance);
            cashierServiceUtils.updateBalance(requestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // Mark participant as left
        participantsModel.setSubWalletBalance(0L);
        participantsModel.setPlayerStatus(EnumConstants.PLAYER_STATUS.LEFT);
        participantsRepository.save(participantsModel);
    }

    @Override
    public void rejoinTable(Long gameId, Long dealId, Long userId) {
        Optional<GameModel> gameModel = gameRepository.findById(gameId);
        if (!gameModel.isPresent()) throw new CustomException(ResponseMessage.GAME_ID_INVALID_ERROR);
        // Game details
        GameModel gameObject = gameModel.get();
        Optional<DealsModel> dealsModel = dealsRepository.findById(dealId);
        if (!dealsModel.isPresent()) throw new CustomException(ResponseMessage.DEAL_NOT_EXIST_ERROR);
        // Deal details
        DealsModel dealObject = dealsModel.get();
        Optional<TableModel> tableModel = tableRepository.findById(gameObject.getTableId());
        if (!tableModel.isPresent()) throw new CustomException(ResponseMessage.TABLE_NOT_EXIST_ERROR);
        // Players table details
        TableModel tableObject = tableModel.get();
        if (tableObject.getGameType() != EnumConstants.RUMMY_TYPE.POOL_101 && tableObject.getGameType() != EnumConstants.RUMMY_TYPE.POOL_201)
            throw new CustomException(ResponseMessage.REJOIN_GAME_TYPE_ERROR);
        ParticipantsModel participantsModel = participantsRepository.findByGameIdAndUserId(gameId, userId, EnumConstants.PLAYER_STATUS.LEFT);
        if (participantsModel == null) throw new CustomException(ResponseMessage.PARTICIPANTS_FETCH_ERROR);
        // Verify wager amount
        BalanceDto balanceDto = cashierServiceUtils.getBalance(userId);
        if (balanceDto.getCashBalance() < tableObject.getMinBuyin()) {
            throw new CustomException(ResponseMessage.INSUFFICIENT_BALANCE_REJOIN);
        }
        BalanceDto deductBalance = new BalanceDto();
        deductBalance.setUserId(userId);
        deductBalance.setGameId(gameId);
        deductBalance.setTransactionType(CommonConstant.TRANSACTION_TYPE_GAME_WAGER);
        deductBalance.setCashBalance(-((long) tableObject.getMinBuyin()));
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String requestBody = objectWriter.writeValueAsString(deductBalance);
            cashierServiceUtils.updateBalance(requestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // Update game total wager
        gameObject.setTotalWager(gameObject.getTotalWager() + (Math.abs(deductBalance.getCashBalance())));
        gameRepository.save(gameObject);
        // Rejoin player history
        RejoinModel rejoinModel = new RejoinModel();
        rejoinModel.setParticipantId(participantsModel.getId());
        rejoinModel.setDealId(dealObject.getId());
        rejoinModel.setAmount(Math.abs(deductBalance.getCashBalance()));
        rejoinRepository.save(rejoinModel);
        // Remove player from eliminated
        participantsModel.setEliminated(false);
        participantsModel.setWager(participantsModel.getWager() + Math.abs(deductBalance.getCashBalance()));
        participantsRepository.save(participantsModel);
        // Update game score with 1st highest score + 1
        Sort sort = Sort.by("playerRank").ascending();
        List<OutcomeModel> outcomeModels = outcomeRepository.findByDealId(dealObject.getId(), EnumConstants.OUTCOME_TYPE.DEAL, sort);
        outcomeModels = outcomeModels.stream().sorted(Comparator.comparing(OutcomeModel::getTotalScore)).collect(Collectors.toList());
        // 2nd highest total score of player
        int secondHighestGameScore = outcomeModels.get(outcomeModels.size() - 1).getGameScore();
        Optional<OutcomeModel> outcomeModelOptional = outcomeModels.stream().filter(x -> x.getUserId().equals(userId)).findAny();
        if (!outcomeModelOptional.isPresent())
            throw new CustomException(ResponseMessage.REJOIN_GAME_SCORE_UPDATE_ERROR);
        OutcomeModel outcomeModel = outcomeModelOptional.get();
        outcomeModel.setRejoin(true);
        outcomeModel.setGameScore(secondHighestGameScore + 1);
        outcomeModel.setTotalScore(secondHighestGameScore + 1);
        outcomeRepository.save(outcomeModel);
    }

}
