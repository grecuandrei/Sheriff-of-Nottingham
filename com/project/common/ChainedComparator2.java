package com.tema1.common;

import com.tema1.goods.Goods;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ChainedComparator2 implements Comparator<Goods> {
    private List<Comparator<Goods>> listComparators;

    @SafeVarargs
    public ChainedComparator2(final Comparator<Goods>... comparators) {
        this.listComparators = Arrays.asList(comparators);
    }

    @Override
    public final int compare(final Goods emp1, final Goods emp2) {
        for (Comparator<Goods> comparator : listComparators) {
            int result = comparator.compare(emp1, emp2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
}
