package com.example.sort;

public class HeapSortTest extends ExtendedBaseAbstractSortTest {

    HeapSortTest() {
        super("Heap Sort", 1000000);
    }

    @Override
    protected void sort(final Integer[] a) {
        HeapSort.sort(a, Integer::compareTo);
    }
}
