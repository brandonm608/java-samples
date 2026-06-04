package com.example.sort;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class MergesortTest extends ExtendedBaseAbstractSortTest {

    MergesortTest() {
        super("Merge Sort", 1000000);
    }

    @Override
    protected void sort(Integer[] a) {
        MergeSort.sort(a, Integer::compareTo);
    }

    @Test
    public void differentialStableSortTest() {
        Integer[] a = createFilledArray(getDifferentialTestLength());
        Integer[] expected = Arrays.copyOfRange(a, 0, a.length);

        Arrays.sort(expected, Integer::compare);
        MergeSort.sort(a, Integer::compare);

        checkStableSort(a, expected);
    }
}
