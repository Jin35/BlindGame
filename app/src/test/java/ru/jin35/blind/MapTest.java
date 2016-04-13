package ru.jin35.blind;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MapTest {

    @Test
    public void testStartPositionTest() throws Exception {
        Map map = new Map(new int[][]{
                new int[]{Map.W, Map.W, Map.W},
                new int[]{Map.W, Map.S, Map.W},
                new int[]{Map.W, Map.W, Map.W}
        });

        Assert.assertTrue(Arrays.equals(new int[]{1, 1}, map.position));
    }

    @Test
    public void testInvalidStartPosition1() throws Exception {
        testIllegalArgumentMapCreation(new int[][]{
                new int[]{Map.W, Map.W, Map.W},
                new int[]{Map.W, 0, Map.W},
                new int[]{Map.W, Map.W, Map.W}
        });
    }

    @Test
    public void testInvalidStartPosition2() throws Exception {
        testIllegalArgumentMapCreation(new int[][]{
                new int[]{Map.S, Map.W, Map.W},
                new int[]{Map.W, Map.W, Map.W},
                new int[]{Map.W, Map.W, Map.W}
        });
    }

    @Test
    public void testInvalidSize1() throws Exception {
        testIllegalArgumentMapCreation(new int[][]{
                new int[]{Map.W, Map.W, Map.W}
        });
    }

    @Test
    public void testInvalidSize2() throws Exception {
        testIllegalArgumentMapCreation(new int[][]{
                new int[]{Map.W}
        });
    }

    @Test
    public void testInvalidSize3() throws Exception {
        testIllegalArgumentMapCreation(new int[][]{
                new int[]{Map.W},
                new int[]{Map.W},
                new int[]{Map.W}
        });
    }

    @Test
    public void testInvalidDimen() throws Exception {
        testIllegalArgumentMapCreation(new int[][]{
                new int[]{Map.W, Map.W, Map.W},
                new int[]{Map.W, Map.W, Map.W},
                new int[]{Map.W, Map.W, Map.W},
                new int[]{Map.W, Map.W, Map.W}
        });
    }

    @Test
    public void testInvalidDimen2() throws Exception {
        testIllegalArgumentMapCreation(new int[][]{
                new int[]{Map.W, Map.W, Map.W, Map.W},
                new int[]{Map.W, Map.W, Map.W, Map.W},
                new int[]{Map.W, Map.W, Map.W, Map.W}
        });
    }

    private void testIllegalArgumentMapCreation(int[][] mapData) {
        Throwable throwable = null;
        try {
            new Map(mapData);
        } catch (Throwable th) {
            throwable = th;
        } finally {
            Assert.assertNotNull(throwable);
            Assert.assertTrue(throwable instanceof IllegalArgumentException);
        }
    }
}