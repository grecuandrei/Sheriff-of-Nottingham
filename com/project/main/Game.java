package com.tema1.main;

import com.tema1.common.ChainedComparator;
import com.tema1.common.CoinsComparator;
import com.tema1.common.Constants;
import com.tema1.common.OrdComparator;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.IllegalGoods;
import com.tema1.goods.LegalGoods;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class Game {
    private int nrPlayers, nrRounds;
    private ArrayList<String> playersName;
    private ArrayList<Integer> assetsId;

    public Game(final int nrPlayers, final int nrRounds,
                final List<String> playersName, final List<Integer> assetsId) {
        this.nrPlayers = nrPlayers;
        this.nrRounds = nrRounds;
        this.playersName = (ArrayList<String>) playersName;
        this.assetsId = (ArrayList<Integer>) assetsId;
    }

    public final void play() {
        ArrayList<Player> players = new ArrayList<>(nrPlayers);
        int id = 1;
        // init players
        for (String p : playersName) {
            if (p.compareToIgnoreCase("Basic") == 0) {
                Basic b = new Basic();
                b.setOrdLine(id);
                players.add(b);
            } else if (p.compareToIgnoreCase("Greedy") == 0) {
                Greedy g = new Greedy();
                g.setOrdLine(id);
                players.add(g);
            } else {
                Bribe b = new Bribe();
                b.setOrdLine(id);
                players.add(b);
            }
            ++id;
        }
        // do the rounds
        for (int i = 1; i <= nrRounds; ++i) {
            for (int j = 1; j <= nrPlayers; ++j) {
                new SubRound(players, j, i, assetsId);
            }
        }

        // transform illegals in their bonus cards
        bonusAdd(players);

        // add profits
        for (Player player:players) {
            player.profitAdd();
        }

        //King Queen
        kingQueen(players);

        //LeaderBoard
        players.sort(new ChainedComparator(
                new CoinsComparator(),
                new OrdComparator()));
        for (Player player:players) {
            System.out.println(player.getOrdLine() - 1
                    + " " + player.getName() + " " + player.getCoins());
        }
    }

    // Transform illegals in bonus cards
    public final void bonusAdd(final ArrayList<Player> leaderBoard) {
        for (Player player:leaderBoard) {
            ArrayList<Goods> addToStall = new ArrayList<>();

            for (Goods good:player.getStall()) {
                if (good.getType() == GoodsType.Illegal) {
                    for (Map.Entry<Goods, Integer> entry: ((IllegalGoods)
                            GoodsFactory.getInstance().getGoodsById(good.getId()))
                                    .getIllegalBonus().entrySet()) {
                        // add the cards from the bonus times the times it needs
                        // in an new array
                        for (int i = 0; i < entry.getValue(); ++i) {
                            addToStall.add(entry.getKey());
                        }
                    }
                }
            }
            // add the bonus in cards on stall
            player.getStall().addAll(addToStall);
        }
    }

    // King Quin
    public final void kingQueen(final ArrayList<Player> players) {
        int[][] idKingQueen = new int[Constants.getSizeKingQueen()][Constants.getSize()];
        int[][] freq = new int[players.size()][Constants.getSize()];

        // make a frequency matrix for every player (lines) for every good (column)
        for (Player player:players) {
            for (Goods good:player.getStall()) {
                if (good.getType() == GoodsType.Legal) {
                    freq[player.getOrdLine() - 1][good.getId()] =
                            Collections.frequency(player.getStall(), good);
                }
            }
        }

        // set the king for every good
        for (int j = 0; j < Constants.getSize(); ++j) {
            for (int i = 0; i < players.size(); ++i) {
                if (freq[i][j] != 0) {
                    if (idKingQueen[0][j] == 0) {
                        idKingQueen[0][j] = i + 1;
                    } else {
                        if (freq[idKingQueen[0][j] - 1][j] < freq[i][j]) {
                            idKingQueen[0][j] = i + 1;
                        }
                    }
                }
            }
        }

        // set the queen for every good
        for (int j = 0; j < Constants.getSize(); ++j) {
            for (int i = 0; i < players.size(); ++i) {
                if (i != idKingQueen[0][j] - 1 && freq[i][j] != 0) {
                    if (idKingQueen[1][j] == 0) {
                        idKingQueen[1][j] = i + 1;
                    } else if (freq[idKingQueen[1][j] - 1][j] < freq[i][j]) {
                        idKingQueen[1][j] = i + 1;
                    }
                }
            }
        }

        for (int i = 0; i < Constants.getSize(); ++i) {
            // add the king bonus per good for every player if exists
            if (idKingQueen[0][i] != 0) {
                players.get(idKingQueen[0][i] - 1).setCoins(
                    players.get(idKingQueen[0][i] - 1).getCoins()
                       + ((LegalGoods) GoodsFactory.getInstance().getGoodsById(i)).getKingBonus());
            }
            // add the queen bonus per good for every player if exists
            if (idKingQueen[1][i] != 0) {
                players.get(idKingQueen[1][i] - 1).setCoins(
                    players.get(idKingQueen[1][i] - 1).getCoins()
                       + ((LegalGoods) GoodsFactory.getInstance().getGoodsById(i)).getQueenBonus());
            }
        }
    }
}
