package com.example.sort;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class HybridSortTest extends BaseAbstractSortTest {

    final HybridSort<Integer> hs = new HybridSort<>(Integer::compare);

    HybridSortTest() {
        super("Hybrid Sort 2", 1000000);
    }

    @Override
    public void sort(Integer[] a) {
        HybridSort.sort(a, Integer::compareTo);
    }

    @Test
    public void hybridQuickSortTest() {
        Integer[] a = new Integer[]{10, 9, 3, 4, 7, 6, 5, 8, 2, 1};
        hs.hybridQuickSort(a,  0, a.length - 1);

        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, a);
    }

    @Test
    public void hybridSortTest() {
        Integer[] a = new Integer[]{10, 9, 3, 4, 7, 6, 5, 8, 2, 1};
        hs.hybridSort(a,  0, a.length - 1);

        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, a);
    }
}
