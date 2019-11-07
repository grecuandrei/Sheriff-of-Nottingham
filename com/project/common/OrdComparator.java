package com.tema1.common;

import com.tema1.main.Player;

import java.util.Comparator;

public class OrdComparator implements Comparator<Player> {
    @Override
    public final int compare(final Player p1, final Player p2) {
        return p1.getOrdLine() - p2.getOrdLine();
    }
}
