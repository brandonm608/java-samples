package com.example.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

public class MergeSortInternalTest extends BaseSortTest {
    MergeSortInternalTest() {
        super("Merge Sort Internal", 1000000);
    }

    private static <T> void sort(T[] a,  Comparator<T> cmp) {
        MergeSortInternal<T> ms = new MergeSortInternal<>(cmp);
        ms.partition(a, a.length);
    }

    @Override
    protected void sort(Integer[] a) {
        sort(a, Integer::compare);
    }

    @Test
    public void differentialStableSortTest() {
        StableTestType[] a = createStableTypeFilledArray(getDifferentialTestLength());
        StableTestType[] expected = Arrays.copyOfRange(a, 0, a.length);

        Arrays.sort(expected, StableTestType::compareTo);
        sort(a, StableTestType::compareTo);

        checkStableSort(a, expected);
    }
}
