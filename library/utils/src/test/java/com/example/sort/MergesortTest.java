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
        StableTestType[] a = createStableTypeFilledArray(getDifferentialTestLength());
        StableTestType[] expected = Arrays.copyOfRange(a, 0, a.length);

        Arrays.sort(expected, StableTestType::compareTo);
        MergeSort.sort(a, StableTestType::compareTo);

        checkStableSort(a, expected);
    }
}
