package com.example.sort;

public class HybridSort2Test extends BaseSortTest {
    HybridSort2Test() {
        super("Hybrid Sort 2", 1000000);
    }

    @Override
    public void sort(Integer[] a) {
        HybridSort2.sort(a, Integer::compareTo);
    }
}
