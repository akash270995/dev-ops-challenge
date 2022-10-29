package com.gameservice.turn.util;

import com.gameservice.turn.constant.EnumConstants;
import org.springframework.stereotype.Component;

@Component
public class RummyUtils {

    public EnumConstants.SUITS getSuit(int card) {
        int pos = (card - 1) / 13;
        return EnumConstants.SUITS.values()[pos];
    }

    public int getCardValue(int card) {
        int cardSuit = (card - 1) / 13;
        return card - (cardSuit * 13);
    }

}
