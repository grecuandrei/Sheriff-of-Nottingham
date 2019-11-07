package com.tema1.main;

import com.tema1.common.ChainedComparator2;
import com.tema1.common.Constants;
import com.tema1.common.IdComparator;
import com.tema1.common.ProfitComparator;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private GoodsFactory g = GoodsFactory.getInstance();
    private int coins, bribe, ordLine, bagDeclaration;
    private boolean illegalDeclaration = false;
    private ArrayList<Goods> assetsInHand = new ArrayList<>(Constants.getSize());
    private ArrayList<Goods> bagToSheriff = new ArrayList<>(Constants.getSize() - 2);
    private ArrayList<Goods> stall = new ArrayList<>();
    private String name;

    public Player() {
        // set the start coins at 80 and bribe 0
        this.coins = Constants.getCoinsAtStart();
        setBribe(0);
    }

    public final void setOrdLine(final int ordLine) {
        this.ordLine = ordLine;
    }

    public final int getOrdLine() {
        return ordLine;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final int getCoins() {
        return coins;
    }

    public final void setCoins(final int coins) {
        this.coins = coins;
    }

    public final ArrayList<Goods> getAssetsInHand() {
        return assetsInHand;
    }

    public final ArrayList<Goods> getBagToSheriff() {
        return bagToSheriff;
    }

    public final int getBagDeclaration() {
        return bagDeclaration;
    }

    public final void setBagDeclaration(final int bagDeclaration) {
        this.bagDeclaration = bagDeclaration;
    }

    public final ArrayList<Goods> getStall() {
        return stall;
    }

    public final int getBribe() {
        return bribe;
    }

    public final void setBribe(final int bribe) {
        this.bribe = bribe;
    }

    public final void setIllegalDeclaration(final boolean illegalDeclaration) {
        this.illegalDeclaration = illegalDeclaration;
    }

    // Add the cards in hand
    final void bag(final ArrayList<Integer> assetsIds) {
        int i = assetsInHand.size(), c = 0;
        for (Integer id : assetsIds) {
            if (i == Constants.getSize()) {
                break;
            }
            assetsInHand.add(g.getGoodsById(id));
            ++i;
            ++c;
        }
        // Remove the cards from the pile
        for (i = 0; i < c; ++i) {
            assetsIds.remove(0);
        }
    }

    // Declaration of the bag for Base player

    /**
     *
     * @param round the number of the round
     * @param player the player that needs to declare his bag
     */
    public void declaration(final int round, final Player player) {
        Map<Goods, Integer> freqInHand = new HashMap<>();
        int countLegal = 0;
        // Count the legal goods
        for (Goods good : player.getAssetsInHand()) {
            if (good.getType() == GoodsType.Legal) {
                countLegal++;
            }
        }

        if (countLegal >= 1) {
            // If there are more and at least one legal card
            // Add the frequency of every legal good in a separate array
            for (Goods good : player.getAssetsInHand()) {
                if (good.getType() == GoodsType.Legal) {
                    freqInHand.putIfAbsent(good,
                            Collections.frequency(player.getAssetsInHand(), good));
                }
            }
            // calculate the maximum frequency in the array
            int freqMax = 0;
            for (Map.Entry<Goods, Integer> entry:freqInHand.entrySet()) {
                if (entry.getValue() > freqMax) {
                    freqMax = entry.getValue();
                }
            }
            // add in a separate array the goods that have the same frequency
            ArrayList<Goods> sameFreq = new ArrayList<>();
            for (Map.Entry<Goods, Integer> entry:freqInHand.entrySet()) {
                if (entry.getValue() == freqMax) {
                    sameFreq.add(entry.getKey());
                }
            }

            // sort them by profit, then by id
            sameFreq.sort(new ChainedComparator2(
                    new ProfitComparator(),
                    new IdComparator()));

            int id = sameFreq.get(0).getId(); // only put goods that are the same
            for (int i = 0; i < freqMax; ++i) { // the number of times they appear in hand
                for (Goods good:sameFreq) {
                    if (id == good.getId()) {
                        player.getBagToSheriff().add(good);
                    }
                }
            }
            player.setBagDeclaration(sameFreq.get(0).getId()); // declare the bag as the first good

        } else {
            // If there are only illegal cards
            ArrayList<Goods> illegalAssets = new ArrayList<>(player.getAssetsInHand());
            illegalAssets.sort(new ProfitComparator());

            // put only one item
            if (player.getCoins() - illegalAssets.get(0).getPenalty() >= 0) {
                player.getBagToSheriff().add(illegalAssets.get(0));
            }

            player.setBagDeclaration(0); // declare as apple
        }
    }

    //final Integer sheriffId final Integer key

    /**
     *
     * @param players the array of the players
     * @param sheriff the sheriff
     * @param assetsIds the assetsIds from the start of the game
     * @param player the player that needs to be checked
     */
    public void inspection(final ArrayList<Player> players, final Player sheriff,
                    final ArrayList<Integer> assetsIds, final Player player) {

        // check bag if he has illegals
        for (Goods good:player.getBagToSheriff()) {
            if (player.getBagDeclaration() != good.getId()) {
                setIllegalDeclaration(true);
                break;
            }
        }
        // if basic has money to verify
        if (sheriff.getCoins() >= Constants.getBasicLowBudget()) {
            // if all the hand is legal
            if (!illegalDeclaration) {
                player.getStall().addAll(player.getBagToSheriff());
                // the sheriff gives money to the player
                for (Goods good : player.getBagToSheriff()) {
                    player.setCoins(player.getCoins() + good.getPenalty());
                    sheriff.setCoins(sheriff.getCoins() - good.getPenalty());
                }
            } else {
                for (Goods good:player.getBagToSheriff()) {
                    // if it.s illegal declared pay the penalty and add to assetsIds the cards
                    if (player.getBagDeclaration() != good.getId()) {
                        player.setCoins(player.getCoins() - good.getPenalty());
                        sheriff.setCoins(sheriff.getCoins() + good.getPenalty());
                        assetsIds.add(good.getId());
                    } else { // else add it
                        player.getStall().add(good);
                    }
                }
            }
        } else {
            // the players gets to keep all his hand illegal or not
            player.getStall().addAll(player.getBagToSheriff());
        }

        // reset Declaration if illegal
        setIllegalDeclaration(false);
    }

    final void profitAdd() {
        for (Goods good:getStall()) {
                setCoins(getCoins() + good.getProfit());
        }
    }
}
