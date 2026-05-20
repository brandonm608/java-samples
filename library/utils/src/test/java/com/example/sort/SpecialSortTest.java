package com.example.sort;

public class SpecialSortTest extends BaseAbstractSortTest {
    SpecialSortTest() {
        super("Special Sort", 15);
    }

    @Override
    public void sort(Integer[] a) {
        SpecialSort<Integer> ss = new SpecialSort<>(Integer::compare);
        ss.sort(a);
    }
}
