package ru.jin35.blind;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidFilterTest {

    private Map goAnywhereMap = new Map(new int[][]{
            new int[]{0, 0, 0},
            new int[]{0, -1, 0},
            new int[]{0, 0, 0}
    });

    private Map cellMap = new Map(new int[][]{
            new int[]{0, 1, 0},
            new int[]{1, -1, 1},
            new int[]{0, 1, 0}
    });

    @Test
    public void testGoAnywhere() throws Exception {
        ValidFilter canGoFilter = new ValidFilter(true, goAnywhereMap);
        ValidFilter cantGoFilter = new ValidFilter(false, goAnywhereMap);
        for (Move move : Move.values()) {
            assertFalse(cantGoFilter.call(move));
            assertTrue(canGoFilter.call(move));
        }
    }

    @Test
    public void testNoWay() throws Exception {
        ValidFilter canGoFilter = new ValidFilter(true, cellMap);
        ValidFilter cantGoFilter = new ValidFilter(false, cellMap);
        for (Move move : Move.values()) {
            assertTrue(cantGoFilter.call(move));
            assertFalse(canGoFilter.call(move));
        }
    }
}