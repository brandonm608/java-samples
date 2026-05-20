package com.example.sort;

public class QuickSortTest extends BaseSortTest {
    QuickSortTest() {
        super("Quick Sort", 1000000);
    }

    @Override
    protected void sort(final Integer[] a) {
        QuickSort.sort(a, Integer::compareTo);
    }
}
