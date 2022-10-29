package com.gameservice.outcome.util;

import com.gameservice.outcome.constant.CommonConstant;
import com.gameservice.outcome.constant.EnumConstants;
import com.gameservice.outcome.constant.ResponseMessage;
import com.gameservice.outcome.dto.*;
import com.gameservice.outcome.exception.CustomException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RummyOutcomeUtils {

    public RummyOutcomeDto getGameScore(RummyGameDto rummyGameDto) {
        return rummyGameDto.getRummyType() == EnumConstants.RUMMY_TYPE.POINTS ? getPointsRummyScore(rummyGameDto) :
                rummyGameDto.getRummyType() == EnumConstants.RUMMY_TYPE.DEALS ? getDealsRummyScore(rummyGameDto) :
                        getPoolsRummyScore(rummyGameDto);
    }

    public RummyOutcomeDto getPointsRummyScore(RummyGameDto rummyGameDto) {
        List<RummyGenerateOutcomeDto> rummyGenerateOutcomeDtos = calculateGameScore(rummyGameDto.getPlayerCardsDto(), rummyGameDto.getWildJoker(), rummyGameDto.getRummyType());
        rummyGenerateOutcomeDtos.forEach(rummyGenerateOutcomeDto -> rummyGenerateOutcomeDto.setChipsWon(rummyGenerateOutcomeDto.getGameScore() != 0 ? (int) -(rummyGenerateOutcomeDto.getGameScore() * rummyGameDto.getPointValue()) : 0));
        int winnerTotalScore = rummyGenerateOutcomeDtos.stream()
                .mapToInt(x -> (int) (x.getGameScore() * rummyGameDto.getPointValue()))
                .sum();
        rummyGenerateOutcomeDtos.stream().filter(x -> x.getGameScore() == 0).findAny().ifPresent(rummyGenerateOutcomeDto -> rummyGenerateOutcomeDto.setChipsWon(winnerTotalScore));
        RummyOutcomeDto rummyOutcomeDto = new RummyOutcomeDto();
        rummyOutcomeDto.setScoreBoard(rummyGenerateOutcomeDtos);
        return rummyOutcomeDto;
    }

    public RummyOutcomeDto getDealsRummyScore(RummyGameDto rummyGameDto) {
        List<RummyPreviousDealOutcomeDto> prevRummyScoreBoardDtos = rummyGameDto.getRummyPreviousDealOutcomeDtos();
        List<RummyGenerateOutcomeDto> rummyGenerateOutcomeDtos = calculateGameScore(rummyGameDto.getPlayerCardsDto(), rummyGameDto.getWildJoker(), rummyGameDto.getRummyType());
        updateTotalScore(prevRummyScoreBoardDtos, rummyGenerateOutcomeDtos, rummyGameDto.getRummyType());
        RummyOutcomeDto rummyOutcomeDto = new RummyOutcomeDto();
        rummyOutcomeDto.setScoreBoard(rummyGenerateOutcomeDtos);
        return rummyOutcomeDto;
    }

    public RummyOutcomeDto getPoolsRummyScore(RummyGameDto rummyGameDto) {
        List<RummyPreviousDealOutcomeDto> prevRummyScoreBoardDtos = rummyGameDto.getRummyPreviousDealOutcomeDtos();
        List<RummyGenerateOutcomeDto> rummyGenerateOutcomeDtos = calculateGameScore(rummyGameDto.getPlayerCardsDto(), rummyGameDto.getWildJoker(), rummyGameDto.getRummyType());
        updateTotalScore(prevRummyScoreBoardDtos, rummyGenerateOutcomeDtos, rummyGameDto.getRummyType());
        canRejoinPoolGame(rummyGenerateOutcomeDtos, rummyGameDto.getRummyType());
        EnumConstants.SPLIT_TYPE splitTypeAvailable = EnumConstants.SPLIT_TYPE.AUTO;
        // Check for Auto Split Pool Game
        for (RummyGenerateOutcomeDto x : rummyGenerateOutcomeDtos) {
            if ((rummyGameDto.getRummyType() == EnumConstants.RUMMY_TYPE.POOL_101 && x.getTotalScore() <= CommonConstant.RUMMY_POOL_101_AUTO_SPLIT_POINTS) || (rummyGameDto.getRummyType() == EnumConstants.RUMMY_TYPE.POOL_201 && x.getTotalScore() <= CommonConstant.RUMMY_POOL_201_AUTO_SPLIT_POINTS)) {
                splitTypeAvailable = EnumConstants.SPLIT_TYPE.NONE;
                break;
            }
        }
        // Check for Manual Split Pool Game
        if (splitTypeAvailable == EnumConstants.SPLIT_TYPE.NONE) {
            splitTypeAvailable = EnumConstants.SPLIT_TYPE.MANUAL;
            for (RummyGenerateOutcomeDto x : rummyGenerateOutcomeDtos) {
                if ((rummyGameDto.getRummyType() == EnumConstants.RUMMY_TYPE.POOL_101 && x.getTotalScore() <= CommonConstant.RUMMY_POOL_101_MANUAL_SPLIT_POINTS) || (rummyGameDto.getRummyType() == EnumConstants.RUMMY_TYPE.POOL_201 && x.getTotalScore() <= CommonConstant.RUMMY_POOL_201_MANUAL_SPLIT_POINTS)) {
                    splitTypeAvailable = EnumConstants.SPLIT_TYPE.NONE;
                    break;
                }
            }
        }
        RummyOutcomeDto rummyOutcomeDto = new RummyOutcomeDto();
        rummyOutcomeDto.setSplitType(splitTypeAvailable);
        rummyOutcomeDto.setScoreBoard(rummyGenerateOutcomeDtos);
        return rummyOutcomeDto;
    }

    private void updateTotalScore(List<RummyPreviousDealOutcomeDto> prevRummyScoreBoardDtos, List<RummyGenerateOutcomeDto> rummyGenerateOutcomeDtos, EnumConstants.RUMMY_TYPE rummyType) {
        rummyGenerateOutcomeDtos.forEach(rummyGenerateOutcomeDto -> {
            if (prevRummyScoreBoardDtos != null) {
                prevRummyScoreBoardDtos.stream()
                        .filter(x -> x.getPlayerId() == rummyGenerateOutcomeDto.getPlayerId())
                        .findAny().ifPresent(x -> rummyGenerateOutcomeDto.setTotalScore(x.getTotalScore() + rummyGenerateOutcomeDto.getGameScore()));
            }
            if ((rummyType == EnumConstants.RUMMY_TYPE.POOL_101 && rummyGenerateOutcomeDto.getTotalScore() >= CommonConstant.RUMMY_POOL_101_POINTS)
                    || (rummyType == EnumConstants.RUMMY_TYPE.POOL_201 && rummyGenerateOutcomeDto.getTotalScore() >= CommonConstant.RUMMY_POOL_201_POINTS)) {
                rummyGenerateOutcomeDto.setEliminated(true);
            }
        });
    }

    private List<RummyGenerateOutcomeDto> calculateGameScore(List<PlayerCardsDto> playerCardsDtos, int wildJoker, EnumConstants.RUMMY_TYPE gameType) {
        List<RummyGenerateOutcomeDto> rummyGenerateOutcomeDtos = new ArrayList<>();
        playerCardsDtos.forEach(playerCardsDto -> {
            int gameScore = 0;
            if (playerCardsDto.getDeclarationType() == EnumConstants.DECLARATION_TYPE.WRONG) {
                gameScore = CommonConstant.RUMMY_WRONG_DECLARATION_POINTS;
            } else if (playerCardsDto.getDropType() == null || playerCardsDto.getDropType() == EnumConstants.DROP_TYPE.NONE || (playerCardsDto.getDropType() == EnumConstants.DROP_TYPE.AUTO_MIDDLE && gameType == EnumConstants.RUMMY_TYPE.DEALS)) {
                if (playerCardsDto.getPureSequences().size() == 0) {
                    gameScore += getTotalScore(playerCardsDto.getImpureSequences(), wildJoker);
                    gameScore += getTotalScore(playerCardsDto.getSets(), wildJoker);
                }
                gameScore += getTotalScore(playerCardsDto.getInvalidCards(), wildJoker);
                gameScore = Math.min(gameScore, CommonConstant.RUMMY_MAX_CAPPED_POINTS);
            } else if (playerCardsDto.getDropType() == EnumConstants.DROP_TYPE.AUTO_MIDDLE) {
                gameScore = gameType == EnumConstants.RUMMY_TYPE.POINTS ? CommonConstant.RUMMY_POINTS_AUTO_MIDDLE_DROP_POINTS :
                        gameType == EnumConstants.RUMMY_TYPE.POOL_101 ? CommonConstant.RUMMY_POOL_101_AUTO_MIDDLE_DROP_POINTS :
                                CommonConstant.RUMMY_POOL_201_AUTO_MIDDLE_DROP_POINTS;
            } else if (gameType == EnumConstants.RUMMY_TYPE.DEALS) {
                throw new CustomException(ResponseMessage.DEALS_DROP_ERROR);
            } else {
                gameScore = playerCardsDto.getDropType() == EnumConstants.DROP_TYPE.FIRST ?
                        (gameType == EnumConstants.RUMMY_TYPE.POINTS ? CommonConstant.RUMMY_POINTS_FIRST_DROP_POINTS :
                                gameType == EnumConstants.RUMMY_TYPE.POOL_101 ? CommonConstant.RUMMY_POOL_101_FIRST_DROP_POINTS :
                                        CommonConstant.RUMMY_POOL_201_FIRST_DROP_POINTS)
                        :
                        (gameType == EnumConstants.RUMMY_TYPE.POINTS ? CommonConstant.RUMMY_POINTS_MIDDLE_DROP_POINTS :
                                gameType == EnumConstants.RUMMY_TYPE.POOL_101 ? CommonConstant.RUMMY_POOL_101_MIDDLE_DROP_POINTS :
                                        CommonConstant.RUMMY_POOL_201_MIDDLE_DROP_POINTS);
            }
            RummyGenerateOutcomeDto rummyGenerateOutcomeDto = new RummyGenerateOutcomeDto();
            rummyGenerateOutcomeDto.setPlayerId(playerCardsDto.getPlayerId());
            rummyGenerateOutcomeDto.setGameScore(gameScore);
            rummyGenerateOutcomeDto.setPureSequences(playerCardsDto.getPureSequences());
            rummyGenerateOutcomeDto.setImpureSequences(playerCardsDto.getImpureSequences());
            rummyGenerateOutcomeDto.setSets(playerCardsDto.getSets());
            rummyGenerateOutcomeDto.setInvalidCards(playerCardsDto.getInvalidCards());
            rummyGenerateOutcomeDto.setWinner(gameScore == 0);
            rummyGenerateOutcomeDtos.add(rummyGenerateOutcomeDto);
        });
        return rummyGenerateOutcomeDtos.stream()
                .sorted(Comparator.comparing(RummyGenerateOutcomeDto::getGameScore))
                .collect(Collectors.toList());
    }

    private int getTotalScore(List<Integer> cards, int wildJoker) {
        int totalPoints = 0;
        for (int card : cards) {
            totalPoints += getCardPoint(card, wildJoker);
        }
        return totalPoints;
    }

    /**
     * @param card can have any value for one or two decks
     * @return card point value is returned
     * for cards from 2 to 10 face value will be returned
     * for cards J, Q, K point value will be 10
     * for card A point value is configurable from CommonConstant
     * for normal joker and wild joker point value will be 0
     * method is compatible with two decks having cards between 1 - 106
     * print jokers are considered on 53 and 106
     */

    private int getCardPoint(int card, int wildJoker) {
        if (card == 53 || card == 106) return 0;
        card = card > 53 ? card - 1 : card;
        wildJoker = wildJoker > 53 ? wildJoker - 1 : wildJoker;
        int cardSuit = (card - 1) / 13;
        int wildJokerSuit = (wildJoker - 1) / 13;
        int cardPos = card - (cardSuit * 13);
        int wildJokerPos = wildJoker - (wildJokerSuit * 13);
        if (wildJokerPos == cardPos) return 0;
        return cardPos == 1 ? CommonConstant.ACE_POINT_VALUE : (cardPos == 11 || cardPos == 12 || cardPos == 13) ? 10 : cardPos;
    }

    /**
     * @param rummyGenerateOutcomeDtos score board of all players
     * @param rummyType                rummy game type (Pool 101, Pool 201)
     *                                 update object if player is eliminated and can rejoin or not
     */
    private void canRejoinPoolGame(List<RummyGenerateOutcomeDto> rummyGenerateOutcomeDtos, EnumConstants.RUMMY_TYPE rummyType) {
        for (RummyGenerateOutcomeDto x : rummyGenerateOutcomeDtos) {
            if (x.getEliminated() != null && x.getEliminated()) {
                rummyGenerateOutcomeDtos = rummyGenerateOutcomeDtos.stream().sorted(Comparator.comparing(RummyGenerateOutcomeDto::getTotalScore)).collect(Collectors.toList());
                int secondHighestScore = rummyGenerateOutcomeDtos.get(rummyGenerateOutcomeDtos.size() - 1).getTotalScore();
                if ((rummyType == EnumConstants.RUMMY_TYPE.POOL_101 && secondHighestScore < CommonConstant.RUMMY_POOL_101_REJOIN_POINTS) ||
                        (rummyType == EnumConstants.RUMMY_TYPE.POOL_201 && secondHighestScore < CommonConstant.RUMMY_POOL_201_REJOIN_POINTS)) {
                    x.setCanRejoin(true);
                }
            }
        }
    }

    private int firstHandShowPoints(EnumConstants.RUMMY_TYPE gameType) {
        return gameType == EnumConstants.RUMMY_TYPE.POINTS ? CommonConstant.RUMMY_POINTS_FIRST_HAND_SHOW_POINTS :
                gameType == EnumConstants.RUMMY_TYPE.DEALS ? CommonConstant.RUMMY_DEALS_FIRST_HAND_SHOW_POINTS :
                        gameType == EnumConstants.RUMMY_TYPE.POOL_101 ? CommonConstant.RUMMY_POOL_101_FIRST_HAND_SHOW_POINTS :
                                CommonConstant.RUMMY_POOL_201_FIRST_HAND_SHOW_POINTS;
    }
}
