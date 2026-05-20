package com.example.sort;

public class QuickSortInternalTest extends BaseSortTest {
    QuickSortInternalTest() {
        super("Quick Sort Internal", 1000000);
    }

    @Override
    protected void sort(Integer[] a) {
        QuickSortInternal<Integer> qs = new QuickSortInternal<>(Integer::compare);
        qs.quickSort(a, 0, a.length - 1);
    }
}
