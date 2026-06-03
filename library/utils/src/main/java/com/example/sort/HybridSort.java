package com.example.sort;

import java.util.Arrays;
import java.util.Comparator;

public class HybridSort<T> extends BaseSort<T> {
    public static <T> void sort(final T[] a,  final Comparator<T> cmp) {
        HybridSort<T> hs = new HybridSort<>(cmp);
        hs.sort(a);
    }

    private final MergeSort<T> ms;
    private final QuickSort<T> qs;
    private final InsertionSort<T> is;

    protected HybridSort(final Comparator<T> cmp) {
        super(cmp);
        this.ms = new MergeSort<>(cmp);
        this.qs = new QuickSort<>(cmp);
        this.is = new InsertionSort<>(cmp);
    }

    public void sort(final T[] a) {
        hybridMergeSort(a, a.length);
    }

    protected void hybridQuickSort(final T[] a, final int start, final int end) {
        if (end < start) {
            return;
        }

        if (end - start > 7) {
            final QuickSort.MutableIntTuple tuple;
            final int startIndex;
            final int endIndex;

            tuple = qs.partition(a, start, end);

            startIndex = tuple.first;
            endIndex = tuple.second;

            hybridQuickSort(a, start, startIndex - 1);
            hybridQuickSort(a, endIndex + 1, end);
            return;
        }

        shortSort(a, start, end);
    }

    protected void hybridMergeSort(final T[] a, final int length) {
        int partition;

        if (length < 1000) {
            hybridSort(a, 0, length - 1);
            return;
        }

        partition = length / 2;

        final T[] b = Arrays.copyOfRange(a, partition, length);

        hybridMergeSort(a, partition);
        hybridMergeSort(b, b.length);

        ms.merge(a, b, partition, length);
    }

    protected void shortSort(final T[] a, final int start, final int end) {
        is.insertionSort(a, start, end);
    }

    protected void hybridSort(final T[] a, final int start, final int end) {
        if (end - start > 8) {
            hybridQuickSort(a, start, end);
            return;
        }

        shortSort(a, start, end);
    }
}
