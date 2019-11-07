package com.tema1.main;

import com.tema1.common.ChainedComparator2;
import com.tema1.common.Constants;
import com.tema1.common.ProfitComparator;
import com.tema1.common.SameFreqProfitComparator;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsType;

import java.util.ArrayList;

public class Bribe extends Player {

    public Bribe() {
        super();
        setName("BRIBED");
    }

    @Override
    public final void declaration(final int round, final Player player) {
        ArrayList<Goods> illegalsInHand = new ArrayList<>();
        ArrayList<Goods> legalsInHand = new ArrayList<>();

        // put illegals and legals goods in different arrays
        for (Goods good:player.getAssetsInHand()) {
            if (good.getType() != GoodsType.Legal) {
                illegalsInHand.add(good);
            } else {
                legalsInHand.add(good);
            }
        }

        // if there are no illegals or the money of the player ar lower than 5
        if (illegalsInHand.size() == 0 || player.getCoins() <= Constants.getMinCoins()) {
            player.setBribe(0);
            // play like a basic
            super.declaration(round, player);
        } else {
            int penalty = 0;

            // if the player has less money than 10, set the maximum hand to 2, else to 8
            int nrItems = player.getCoins() < Constants.getHighBribe()
                    ? Constants.getMaxBagLowCash() : Constants.getMaxHand();
            player.setBagDeclaration(0);

            // sort illegals after profit
            illegalsInHand.sort(new ProfitComparator());

            // add illegals in hand as much as he can
            for (int i = 0; i < nrItems && i < illegalsInHand.size(); ++i) {
                penalty += illegalsInHand.get(i).getPenalty();
                if (player.getCoins() - penalty > 0) {
                    player.getBagToSheriff().add(illegalsInHand.get(i));
                } else {
                    penalty -= illegalsInHand.get(i).getPenalty();
                }
            }

            // set bribe to 5 if he has less than 2 illegals, or to 10 if it has 3 or more
            player.setBribe(player.getBagToSheriff().size() <= Constants.getMaxBagLowCash()
                    ? Constants.getLowBribe() : Constants.getHighBribe());

            // if the hand is not full yet
            if (getBagToSheriff().size() < nrItems && legalsInHand.size() != 0) {

                // sort the legals after profit and frequency
                legalsInHand.sort(new ChainedComparator2(
                        new ProfitComparator(),
                        new SameFreqProfitComparator()));

                int ok = 0;
                for (Goods good : legalsInHand) {
                    penalty += good.getPenalty();
                    // add legals if possible
                    if (player.getCoins() - penalty >= 1
                            && player.getBagToSheriff().size() < nrItems) {
                        player.getBagToSheriff().add(good);
                    }
                    // when the player has 9 coins, although the amount
                    // is less than 10 and the nrItems is set on 2
                    // he can add one more legal:
                    // 4 + 2 + 2 = 8 => 9 - 8 >= 1 (the bottom limit for bribe)
                    if (player.getCoins() == Constants.getMagicNumber()
                            && player.getCoins() - penalty >= 1 && ok == 0) {
                        player.getBagToSheriff().add(good);
                        ok = 1;
                    }
                }
            }
        }
    }

    @Override
    // it will be done just one time as the bribe checks two players
    // and gets the bribe from the others
    public final void inspection(final ArrayList<Player> players, final Player sheriff,
                    final ArrayList<Integer> assetsIds, final Player player) {
        // player left
        Player leftPlayer = players.get(sheriff.getOrdLine() - 1 == 0
                ? players.size() - 1 : sheriff.getOrdLine() - 2);
        // player right
        Player rightPlayer = players.get((sheriff.getOrdLine()) % players.size());

        if (players.size() == 2) {
            // checks only the other player
            super.inspection(players, sheriff, assetsIds, leftPlayer);
        } else {
            // inspect only the two of them
            super.inspection(players, sheriff, assetsIds, leftPlayer);
            super.inspection(players, sheriff, assetsIds, rightPlayer);

            // and takes the bribe from the rest
            for (Player player1:players) {
                if (player1 != leftPlayer && player1 != rightPlayer
                        && player1 != sheriff) {
                    if (player1.getBribe() != 0) {
                        sheriff.setCoins(sheriff.getCoins() + player1.getBribe());
                        player1.setCoins(player1.getCoins() - player1.getBribe());
                        player1.setBribe(0);
                    }
                    // and add the bag to stall
                    for (Goods good:player1.getBagToSheriff()) {
                        player1.getStall().add(good);
                    }
                }
            }
        }
    }
}
