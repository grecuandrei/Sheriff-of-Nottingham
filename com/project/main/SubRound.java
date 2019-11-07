package com.tema1.main;

import java.util.ArrayList;

public class SubRound {
    private Integer sheriff;

    public SubRound(final ArrayList<Player> players, final int ordLine,
                    final int round, final ArrayList<Integer> assetsIds) {
        for (Player player : players) {
            player.setBribe(0);
            if (ordLine != player.getOrdLine()) {
                // Creation
                player.bag(assetsIds);
                // Declaration
                player.declaration(round, player);
            } else {
                // set the sheriff
                sheriff = player.getOrdLine();
            }
        }

        // Inspection
        if (players.get(sheriff - 1).getName().equals("BRIBED")) {
            // for bribed - sheriff we do inspection only once
            players.get(sheriff - 1).inspection(players, players.get(sheriff - 1), assetsIds,
                    players.get(0));
        } else {
            // else for everyone else without the sheriff
            for (Player player : players) {
                if (players.get(sheriff - 1).getOrdLine() != player.getOrdLine()) {
                    players.get(sheriff - 1).inspection(players, players.get(sheriff - 1),
                            assetsIds, player);
                }
            }
        }

        // clear hand and the bag to the sheriff
        for (Player player : players) {
            player.getAssetsInHand().clear();
            player.getBagToSheriff().clear();
        }
    }
}
