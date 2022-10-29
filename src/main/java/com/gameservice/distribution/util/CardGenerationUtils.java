package com.gameservice.distribution.util;

import com.gameservice.distribution.dto.CardsGeneratedDto;
import com.gameservice.outcome.constant.EnumConstants;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CardGenerationUtils {

    static String ALGORITHM = "SHA1PRNG";

    static String PROVIDER = "SUN";

    public CardsGeneratedDto getDistributionCards(int totalPlayers, int totalDecks, EnumConstants.GAME_SUB_TYPE cardsType) {
        CardsGeneratedDto cardsGeneratedDto = new CardsGeneratedDto();
        int cardsPerPlayer = cardsType == EnumConstants.GAME_SUB_TYPE.CARDS_10 ? 10 :
                cardsType == EnumConstants.GAME_SUB_TYPE.CARDS_13 ? 13 :
                        21;
        int bound = totalDecks * 53;
        int length = cardsPerPlayer * totalPlayers;
        List<Integer> hands = Arrays.stream(generateRandomCards(bound, length)).boxed().collect(Collectors.toList());
        AtomicInteger counter = new AtomicInteger();
        cardsGeneratedDto.setPlayersCards(new ArrayList<>(hands.stream().collect(Collectors.groupingBy(i -> counter.getAndIncrement() / cardsPerPlayer)).values()));
        List<Integer> closedDeckCards = (IntStream.rangeClosed(1, bound).boxed().collect(Collectors.toList()));
        closedDeckCards.removeAll(hands);
        cardsGeneratedDto.setWildJoker(generateRandomCards(bound, 1)[0]);
        closedDeckCards = Arrays.stream(shuffleCards(closedDeckCards.stream().mapToInt(i -> i).toArray())).boxed().collect(Collectors.toList());
        cardsGeneratedDto.setClosedDeckCards(closedDeckCards);
        return cardsGeneratedDto;
    }

    public List<Integer> generateTossCards(int totalPlayers, int totalDecks) {
        int bound = totalDecks * 52;
        return Arrays.stream(generateRandomCards(bound, totalPlayers)).boxed().collect(Collectors.toList());
    }

    public List<Integer> getShuffledCards(int[] cards) {
        return Arrays.stream(shuffleCards(cards)).boxed().collect(Collectors.toList());
    }

    public static int[] generateRandomCards(int bound, int length) {
        Set<Integer> cards = new HashSet<>();
        SecureRandom secureRandomGenerator;
        try {
            secureRandomGenerator = SecureRandom.getInstance(ALGORITHM, PROVIDER);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        // Get 128 random bytes
        byte[] randomBytes = new byte[128];
        secureRandomGenerator.nextBytes(randomBytes);
        int random;
        while (cards.size() != length) {
            //Get random integer
            random = secureRandomGenerator.nextInt(bound);
            if (random != 0) cards.add(random);
        }
        return cards.stream().mapToInt(i -> i).toArray();
    }

    public static int[] shuffleCards(int[] cards) {
        SecureRandom secureRandomGenerator;
        try {
            secureRandomGenerator = SecureRandom.getInstance(ALGORITHM, PROVIDER);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        // Get 128 random bytes
        byte[] randomBytes = new byte[128];
        secureRandomGenerator.nextBytes(randomBytes);
        int random;
        int i = 0;
        while (i != cards.length) {
            //Get random integer
            random = i + secureRandomGenerator.nextInt(cards.length - i);
            //swapping the cards
            int temp = cards[random];
            cards[random] = cards[i];
            cards[i] = temp;
            i++;
        }
        return cards;
    }

}
