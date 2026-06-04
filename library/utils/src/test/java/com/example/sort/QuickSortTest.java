package com.example.sort;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class QuickSortTest extends BaseAbstractSortTest {
    
    final QuickSort<Integer> qs = new QuickSort<>(Integer::compare);

    QuickSortTest() {
        super("Quick Sort", 1000000);
    }

    @Override
    protected void sort(final Integer[] a) {
        QuickSort.sort(a, Integer::compareTo);
    }

    @Test
    public void medianOf3SwapTest1() {
        Integer[] a = {1, 2, 3};
        qs.medianOf3Swap(a, 0, 1, 2);
        assertArrayEquals(new Integer[]{1, 3, 2}, a);
    }

    @Test
    public void medianOf3SwapTest2() {
        Integer[] a = {3, 2, 1};
        qs.medianOf3Swap(a, 0, 1, 2);
        assertArrayEquals(new Integer[]{3, 1, 2}, a);
    }

    @Test
    public void medianOf3SwapTest3() {
        Integer[] a = {1, 3, 2};
        qs.medianOf3Swap(a, 0, 1, 2);
        assertArrayEquals(new Integer[]{1, 3, 2}, a);
    }

    @Test
    public void medianOf3SwapTest4() {
        Integer[] a = {2, 1, 3};
        qs.medianOf3Swap(a, 0, 1, 2);
        assertArrayEquals(new Integer[]{3, 1, 2}, a);
    }

    @Test
    public void medianOf3SwapTest5() {
        Integer[] a = {2, 3, 1};
        qs.medianOf3Swap(a, 0, 1, 2);
        assertArrayEquals(new Integer[]{1, 3, 2}, a);
    }

    @Test
    public void medianOf3SwapTest6() {
        Integer[] a = {3, 1, 2};
        qs.medianOf3Swap(a, 0, 1, 2);
        assertArrayEquals(new Integer[]{3, 1, 2}, a);
    }

    @Test
    public void randomTest() {
        Integer[] a = new Integer[]{10, 9, 3, 4, 7, 6, 5, 8, 2, 1};
        qs.quickSort(a,  0, a.length - 1);

        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, a);
    }
}
