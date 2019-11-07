package com.tema1.common;

import com.tema1.main.Player;

import java.util.Comparator;

public final class CoinsComparator implements Comparator<Player> {
    @Override
    public int compare(final Player p1, final Player p2) {
        return p2.getCoins() - p1.getCoins();
    }
}
