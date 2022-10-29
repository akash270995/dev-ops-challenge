package com.gameservice.outcome.util;

import com.gameservice.outcome.constant.CommonConstant;
import com.gameservice.outcome.constant.EnumConstants;
import com.gameservice.outcome.dto.PlayerPrizeDto;
import com.gameservice.outcome.dto.RummyGenerateOutcomeDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class RummyPrizeUtils {

    /**
     * @param totalWager  sum of all players bet amount
     * @param rakePercent rake percent
     * @return total prize money that is to be distributed among players
     */
    public long calculatePrizeMoney(int totalWager, int rakePercent) {
        return totalWager - calculateHouseFee(totalWager, rakePercent);
    }

    /**
     * @param totalWager  sum of all players wager amount in entire game
     * @param rakePercent rake percent
     * @return total rake amount or house fee
     */
    public long calculateHouseFee(long totalWager, int rakePercent) {
        return (totalWager * rakePercent) / 100;
    }

    public PlayerPrizeDto calculatePointsPrize(int losingPlayersPoints, double pointValue, int rakePercent) {
        PlayerPrizeDto playerPrizeDto = new PlayerPrizeDto();
        long totalWager = (long) (losingPlayersPoints * pointValue);
        long totalRake = calculateHouseFee(totalWager, rakePercent);
        playerPrizeDto.setPrizeAmount(totalWager - totalRake);
        playerPrizeDto.setTotalRake(totalRake);
        return playerPrizeDto;
    }

    public PlayerPrizeDto calculatePoolsAndDealsPrize(long totalWager, int rakePercent) {
        PlayerPrizeDto playerPrizeDto = new PlayerPrizeDto();
        long totalRake = calculateHouseFee(totalWager, rakePercent);
        playerPrizeDto.setPrizeAmount(totalWager - totalRake);
        playerPrizeDto.setTotalRake(totalRake);
        return playerPrizeDto;
    }

    /**
     * @param rummyGenerateOutcomeDtos outcome generated for the game
     * @param playersPrizeAmount       betting amount of all players after deducting the rake/house fee amount
     * @param totalPlayers             total number of the players
     * @return
     */
    public List<PlayerPrizeDto> autoSplitPoolGamePrizes(List<RummyGenerateOutcomeDto> rummyGenerateOutcomeDtos, long playersPrizeAmount, long totalPlayers) {
        long prizeAmount = playersPrizeAmount / totalPlayers;
        List<PlayerPrizeDto> playerPrizeDtos = new ArrayList<>();
        rummyGenerateOutcomeDtos.forEach(x -> {
            PlayerPrizeDto playerPrizeDto = new PlayerPrizeDto();
            playerPrizeDto.setPlayerId(x.getPlayerId());
            playerPrizeDto.setPrizeAmount(prizeAmount);
            playerPrizeDto.setSplitType(EnumConstants.SPLIT_TYPE.AUTO);
            playerPrizeDtos.add(playerPrizeDto);
        });
        return playerPrizeDtos;
    }

    /**
     * @param rummyGenerateOutcomeDtos outcome generated for the game
     * @param rummyType                type of pool game
     * @param playersPrizeAmount       betting amount of all players after deducting the rake/house fee amount
     * @param totalPlayers             total number of the players
     * @return
     */
    public List<PlayerPrizeDto> manualSplitPoolGamePrizes(List<RummyGenerateOutcomeDto> rummyGenerateOutcomeDtos, EnumConstants.RUMMY_TYPE rummyType, long playersPrizeAmount, long totalPlayers) {
        int maxDrop = findMaxDropAvailable(rummyGenerateOutcomeDtos, rummyType);
        List<PlayerPrizeDto> playerPrizeDtos = new ArrayList<>();
        rummyGenerateOutcomeDtos.forEach(x -> {
            long playerPrizeAmount = calculateSplitPrize(x.getAvailableDrops() < maxDrop, playersPrizeAmount, totalPlayers);
            PlayerPrizeDto playerPrizeDto = new PlayerPrizeDto();
            playerPrizeDto.setPlayerId(x.getPlayerId());
            playerPrizeDto.setPrizeAmount(playerPrizeAmount);
            playerPrizeDto.setSplitType(EnumConstants.SPLIT_TYPE.MANUAL);
            playerPrizeDtos.add(playerPrizeDto);
        });
        return playerPrizeDtos;
    }

    /**
     * @param haveLessDrops      player having the least number of drops
     * @param playersPrizeAmount betting amount of all players after deducting the rake/house fee amount
     * @param totalPlayers       total number of the players
     * @return
     */
    private long calculateSplitPrize(boolean haveLessDrops, long playersPrizeAmount, long totalPlayers) {
        long playerPrizeAmount = playersPrizeAmount / totalPlayers;
        if (haveLessDrops) {
            playerPrizeAmount = playerPrizeAmount / totalPlayers;
        } else {
            playerPrizeAmount += playerPrizeAmount / totalPlayers;
        }
        return playerPrizeAmount;
    }

    private int findMaxDropAvailable(List<RummyGenerateOutcomeDto> rummyGenerateOutcomeDtos, EnumConstants.RUMMY_TYPE rummyType) {
        List<Integer> playersDrop = new ArrayList<>();
        rummyGenerateOutcomeDtos.forEach(x -> {
            int dropsAvailable;
            if (rummyType == EnumConstants.RUMMY_TYPE.POOL_101) {
                dropsAvailable = (100 - x.getTotalScore()) / CommonConstant.RUMMY_POOL_101_FIRST_DROP_POINTS;
            } else {
                dropsAvailable = (200 - x.getTotalScore()) / CommonConstant.RUMMY_POOL_201_FIRST_DROP_POINTS;
            }
            playersDrop.add(dropsAvailable);
            x.setAvailableDrops(dropsAvailable);
        });
        Collections.sort(playersDrop);
        return playersDrop.get(playersDrop.size() - 1);
    }

}
