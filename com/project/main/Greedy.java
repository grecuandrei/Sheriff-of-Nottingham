package com.tema1.main;

import com.tema1.common.Constants;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Greedy extends Player {

    public Greedy() {
        super();
        setName("GREEDY");
    }

    @Override
    public final void declaration(final int round, final Player player) {
        Map<Integer, Goods> sort = new HashMap<>(player.getAssetsInHand().size());
        ArrayList<Goods> illegalToAdd = new ArrayList<>();
        int i = 0;

        // plays first as a basic player
        super.declaration(round, player);

        // if the hand is even add illegals
        if (getBagToSheriff().size() < Constants.getMaxHand() && round % 2 == 0) {
            // save an array with illegals
            for (Goods good:player.getAssetsInHand()) {
                if (good.getType() == GoodsType.Illegal) {
                    illegalToAdd.add(good);
                }
            }

            // if there are more than one illegals
            if (illegalToAdd.size() > 1) {
                int penalty = 0;
                HashMap<Goods, Integer> freqMap = new HashMap<>();

                // creating a map for good and the frequency
                for (Goods good : illegalToAdd) {
                    if (freqMap.containsKey(good)) {
                        // If number is present in freqMap incrementing it's count by 1
                        freqMap.put(good, freqMap.get(good) + 1);
                    } else {
                        // If integer is not present in freqMap putting
                        // this integer to freqMap with 1 as it's value
                        freqMap.put(good, 1);
                    }
                }

                // eliminate the instances that are in the bag
                boolean exit = true;
                for (Goods key : freqMap.keySet()) {
                    if (exit) {
                        for (Goods good : player.getBagToSheriff()) {
                            if (key.getId() == good.getId()) {
                                freqMap.put(key, freqMap.get(key) - 1);
                                exit = false;
                                break;
                            }
                        }
                    }
                }
                // eliminate the illegals that are not appearing after the modification
                for (Map.Entry<Goods, Integer> entry : freqMap.entrySet()) {
                    if (entry.getValue() == 0) {
                        freqMap.remove(entry.getKey());
                        break;
                    }
                }

                // sorting after max profit
                int maxProf = 0;
                for (Goods key : freqMap.keySet()) {
                    if (key.getProfit() > maxProf) {
                        maxProf = key.getProfit();
                    }
                }

                // add the goods that have the bigger profit
                // but checking the money of the player every time
                for (Goods key : freqMap.keySet()) {
                    penalty += key.getPenalty();
                    if (key.getProfit() == maxProf && getCoins() - penalty >= 0) {
                        getBagToSheriff().add(key);
                    }
                }

            } else if (illegalToAdd.size() == 1) { // if there is just one illegal good
                if (getCoins() - illegalToAdd.get(0).getProfit()
                        - getBagToSheriff().get(0).getPenalty() >= 0) {
                    getBagToSheriff().add(illegalToAdd.get(0));
                }
            }

            // if there is only one illegal in bag
            if (getBagToSheriff().get(0).getType() == GoodsType.Illegal
                    && getBagToSheriff().size() == 1) {
                int allow2 = 0;
                // copy in a different array - sort
                for (Goods good:getAssetsInHand()) {
                    if (good.getId() == getBagToSheriff().get(0).getId()) {
                        sort.put(i, good);
                        getBagToSheriff().clear();
                    }
                    if (good.getId() == illegalToAdd.get(1).getId() && allow2 == 0) {
                        sort.put(i, good);
                        allow2 = 1;
                    }
                    ++i;
                }

                // add the array to the hand
                for (Map.Entry<Integer, Goods> entry:sort.entrySet()) {
                    getBagToSheriff().add(entry.getValue());
                }
            }
        }
    }

    @Override
    public final void inspection(final ArrayList<Player> players, final Player sheriff,
                    final ArrayList<Integer> assetsIds, final Player player) {
        // if the bribe is 0 check the player
        if (player.getBribe() == 0) {
            super.inspection(players, sheriff, assetsIds, player);
        } else { // else take the bribe from him
            sheriff.setCoins(
                    sheriff.getCoins() + player.getBribe());
            player.setCoins(player.getCoins() - player.getBribe());
            player.setBribe(0);
            // add all the goods from the bag to stall
            for (Goods good:player.getBagToSheriff()) {
                player.getStall().add(good);
            }
        }
    }

}
