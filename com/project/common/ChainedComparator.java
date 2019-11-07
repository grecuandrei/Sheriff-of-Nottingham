package com.tema1.common;

import com.tema1.main.Player;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ChainedComparator implements Comparator<Player> {
    private List<Comparator<Player>> listComparators;

    @SafeVarargs
    public ChainedComparator(final Comparator<Player>... comparators) {
        this.listComparators = Arrays.asList(comparators);
    }

    @Override
    public final int compare(final Player emp1, final Player emp2) {
        for (Comparator<Player> comparator : listComparators) {
            int result = comparator.compare(emp1, emp2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
}
