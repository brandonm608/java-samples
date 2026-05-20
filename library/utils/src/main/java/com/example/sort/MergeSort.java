package com.example.sort;

import java.util.Comparator;

public class MergeSort<T> implements Sort<T> {
    public static <T> void sort(final T[] a, final Comparator<T> cmp) {
        MergeSort<T> ms = new MergeSort<>(cmp);
        ms.sort(a);
    }

    private final MergeSortInternal<T> ms;

    MergeSort(final Comparator<T> cmp) {
        ms = new MergeSortInternal<>(cmp);
    }

    @Override
    public void sort(T[] a) {
        ms.sort(a);
    }
}
