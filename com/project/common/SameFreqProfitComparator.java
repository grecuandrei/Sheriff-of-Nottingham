package com.tema1.common;

import com.tema1.goods.Goods;

import java.util.Comparator;

public final class SameFreqProfitComparator implements Comparator<Goods> {
    @Override
    public int compare(final Goods g1, final Goods g2) {
        return g2.getId() - g1.getId();
    }
}
