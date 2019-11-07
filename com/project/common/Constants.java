package com.tema1.common;

public final class Constants {
    private Constants() {
    }

    private static final int SIZE = 10;
    private static final int COINS_AT_START = 80;
    private static final int MAX_HAND = 8;
    private static final int MAX_BAG_LOW_CASH = 2;
    private static final int MIN_COINS = 5;
    private static final int LOW_BRIBE = 5;
    private static final int HIGH_BRIBE = 10;
    private static final int BASIC_LOW_BUDGET = 16;
    private static final int SIZE_KING_QUEEN = 2;
    private static final int MAGIC_NUMBER = 9;

    public static int getSize() {
        return SIZE;
    }

    public static int getCoinsAtStart() {
        return COINS_AT_START;
    }

    public  static int getMaxHand() {
        return MAX_HAND;
    }

    public static int getMaxBagLowCash() {
        return MAX_BAG_LOW_CASH;
    }

    public static int getMinCoins() {
        return MIN_COINS;
    }

    public static int getLowBribe() {
        return LOW_BRIBE;
    }

    public static int getHighBribe() {
        return HIGH_BRIBE;
    }

    public static int getBasicLowBudget() {
        return BASIC_LOW_BUDGET;
    }

    public static int getSizeKingQueen() {
        return SIZE_KING_QUEEN;
    }

    public static int getMagicNumber() {
        return MAGIC_NUMBER;
    }
}
