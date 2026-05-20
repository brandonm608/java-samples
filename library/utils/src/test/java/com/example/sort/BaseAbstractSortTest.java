package com.example.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public abstract class BaseAbstractSortTest {
    private final int differentialTestLength;
    private final String sortName;

    BaseAbstractSortTest(final String sortName, final int differentialTestLength) {
        this.sortName = sortName;
        this.differentialTestLength = differentialTestLength;
    }

    protected static class StableTestType implements Comparable<StableTestType> {
        private final int order;
        private final int dummy;

        StableTestType(final int order, final int dummy) {
            this.order = order;
            this.dummy = dummy;
        }

        @Override
        public int compareTo(StableTestType stableTestType) {
            return Integer.compare(this.order, stableTestType.order);
        }
    }

    protected <T> void checkStableSort(final T[] a, final T[] expected) {
        assertEquals(expected.length, a.length, "The array sorted by the " + sortName + " is not the same length as the JDK sorted array!");

        for (int i = 0; i < a.length; i++) {
            assertSame(a[i], expected[i], "Out of order element detected between " + sortName + " sorted array and the JDK sorted array!");
        }
    }

    protected void checkArray(final Integer[] a, final Integer[] expected) {
        assertArrayEquals(expected, a, "The array sorted by the " + sortName + " does not match the JDK sorted array!");
    }

    protected StableTestType[] createStableTypeFilledArray(final int length) {
        StableTestType[] a = new StableTestType[length];

        for (int i = 0; i < length; i++) {
            a[i] = new StableTestType((int)(Math.random() * 500), (int)(Math.random() * Integer.MAX_VALUE));
        }

        return a;
    }

    protected Integer[] createFilledArray(final int length) {
        Integer[] a = new Integer[length];

        for (int i = 0; i < length; i++) {
            a[i] = (int) (Math.random() * 500);
        }

        return a;
    }

    protected int getDifferentialTestLength() {
        return differentialTestLength;
    }

    protected void jdkSortCompare(Integer[] a) {
        Integer[] copy = Arrays.copyOf(a, a.length);

        Arrays.sort(copy, Integer::compareTo);
        this.sort(a);

        checkArray(a, copy);
    }

    @Test
    public void randomArrayTest() {
        Integer[] a = { 10, 5, 3, 4, 4, 9 , 8, 2, 5 };
        Integer[] expected = { 2, 3, 4, 4, 5, 5, 8, 9, 10 };

        this.sort(a);

        assertArrayEquals(expected, a);
    }

    @Test
    public void randomDifferentialTest1() {
        randomDifferentialTestImpl();
    }

    @Test
    public void randomDifferentialTest2() {
        randomDifferentialTestImpl();
    }

    @Test
    public void randomDifferentialTest3() {
        randomDifferentialTestImpl();
    }

    private void randomDifferentialTestImpl() {
        Integer[] a = createFilledArray(getDifferentialTestLength());

        jdkSortCompare(a);
    }

    @Test
    public void reversedDifferentialTest() {
        reversedDifferentialTestImpl();
    }

    private void reversedDifferentialTestImpl() {
        Integer[] a = createFilledArray(getDifferentialTestLength());
        Arrays.sort(a, (i1, i2) -> Integer.compare(i2, i1));

        jdkSortCompare(a);
    }

    @Test
    public void reversedSortedArrayTest() {
        Integer[] a = { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
        Integer[] expected = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        this.sort(a);

        assertArrayEquals(expected, a);
    }

    @Test
    public void sequenceExceptForEnd() {
        Integer[] a = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 1 };
        Integer[] expected = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        this.sort(a);

        assertArrayEquals(expected, a);
    }

    @Test
    public void sequenceExceptForMid() {
        Integer[] a = { 1, 2, 3, 4, 5, 10, 6, 7, 8, 9 };
        Integer[] expected = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        this.sort(a);

        assertArrayEquals(expected, a);
    }

    @Test
    public void sequenceExceptForStart() {
        Integer[] a = { 10, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        Integer[] expected = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        this.sort(a);

        assertArrayEquals(expected, a);
    }

    protected abstract void sort(final Integer[] a);

    @Test
    public void sortedArrayTest() {
        Integer[] a = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        Integer[] expected = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        this.sort(a);

        assertArrayEquals(expected, a);
    }

    @Test
    public void sortedDifferentialTest() {
        sortedDifferentialTestImpl();
    }

    private void sortedDifferentialTestImpl() {
        Integer[] a = createFilledArray(getDifferentialTestLength());
        Arrays.sort(a, Integer::compareTo);

        jdkSortCompare(a);
    }

    @Test
    public void threeItemSortTest1() {
        Integer[] a = { 1, 2, 3 };
        Integer[] expected = { 1, 2 ,3 };

        this.sort(a);

        assertArrayEquals(expected, a);
    }

    @Test
    public void threeItemSortTest2() {
        Integer[] a = { 1, 3, 2 };
        Integer[] expected = { 1, 2 ,3 };

        this.sort(a);

        assertArrayEquals(expected, a);
    }

    @Test
    public void threeItemSortTest3() {
        Integer[] a = { 2, 1, 3 };
        Integer[] expected = { 1, 2 ,3 };

        this.sort(a);

        assertArrayEquals(expected, a);
    }

    @Test
    public void threeItemSortTest4() {
        Integer[] a = { 2, 3, 1 };
        Integer[] expected = { 1, 2 ,3 };

        this.sort(a);

        assertArrayEquals(expected, a);
    }

    @Test
    public void threeItemSortTest5() {
        Integer[] a = { 3, 1, 2 };
        Integer[] expected = { 1, 2 ,3 };

        this.sort(a);

        assertArrayEquals(expected, a);
    }

    @Test
    public void threeItemSortTest6() {
        Integer[] a = { 3, 2, 1 };
        Integer[] expected = { 1, 2 ,3 };

        this.sort(a);

        assertArrayEquals(expected, a);
    }
}
