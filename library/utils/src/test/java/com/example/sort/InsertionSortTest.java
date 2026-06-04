package com.example.sort;

public class InsertionSortTest extends BaseAbstractSortTest {

    InsertionSortTest() {
        super("Insertion Sort", 15);
    }

    @Override
    public void sort(Integer[] a) {
        InsertionSort<Integer> ss = new InsertionSort<>(Integer::compare);
        ss.sort(a);
    }
}
