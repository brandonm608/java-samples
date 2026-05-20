package com.example.sort;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public abstract class BaseSortTest extends BaseAbstractSortTest {
    public BaseSortTest(final String sortName, final int differentialTestLength) {
        super(sortName, differentialTestLength);
    }

    @Test
    public void emptySortTest() {
        Integer[] a = {};
        Integer[] expected = {};

        this.sort(a);

        assertArrayEquals(expected, a);
    }

    @Test
    public void oneItemSortTest() {
        Integer[] a = { 1 };
        Integer[] expected = { 1 };

        this.sort(a);

        assertArrayEquals(expected, a);
    }

    @Test
    public void twoItemSortTest1() {
        Integer[] a = { 2, 1 };
        Integer[] expected = { 1, 2 };

        this.sort(a);

        assertArrayEquals(expected, a);
    }

    @Test
    public void twoItemSortTest2() {
        Integer[] a = { 1, 2 };
        Integer[] expected = { 1, 2 };

        this.sort(a);

        assertArrayEquals(expected, a);
    }
}
