package com.example.sort;

public class InsertionSortTest extends BaseAbstractSortTest {

    InsertionSortTest() {
        super("Special Sort", 15);
    }

    @Override
    public void sort(Integer[] a) {
        InsertionSort<Integer> ss = new InsertionSort<>(Integer::compare);
        ss.sort(a);
    }
}
