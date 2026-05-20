package com.example.sort;

import java.util.Arrays;
import java.util.Comparator;

public class HybridSort2<T> extends BaseSort<T> {
    public static <T> void sort(final T[] a,  final Comparator<T> cmp) {
        HybridSort2<T> hs = new HybridSort2<>(cmp);
        hs.sort(a);
    }

    private final MergeSortInternal<T> ms;
    private final QuickSortInternal<T> qs;
    private final SpecialSort<T> ss;

    protected HybridSort2(final Comparator<T> cmp) {
        super(cmp);
        this.ms = new MergeSortInternal<>(cmp);
        this.qs = new QuickSortInternal<>(cmp);
        this.ss = new SpecialSort<>(cmp);
    }

    public void sort(final T[] a) {
        hybridMergeSort(a, a.length);
    }

    protected void hybridQuickSort(final T[] a, final int start, final int end) {
        final int range = end - start;

        if (range > 7) {
            final QuickSortInternal.MutableIntTuple tuple;
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

    protected void hybridMergeSort(final T[] a, final int end) {
        int partition;

        if (end < 1000) {
            hybridSort(a,0, end - 1);
            return;
        }

        partition = end / 2;

        final T[] b = Arrays.copyOfRange(a, partition, end);

        hybridMergeSort(a, partition);
        hybridMergeSort(b, b.length);

        ms.merge(a, b, partition, end);
    }

    protected void shortSort(final T[] a, final int start, final int end) {
        final int range = end - start;

        // 3 to 7 items
        if (range > 1) {
            ss.specialSort(a, start, end);
            return;
        }

        // 2 items
        if (range > 0) {
            if (isLeft(a[end], a[start])) {
                swap(a, start, end);
            }
        }
    }

    protected void hybridSort(final T[] a, final int start, final int end) {
        if (end - start > 8) {
            hybridQuickSort(a, start, end);
            return;
        }

        shortSort(a, start, end);
    }
}
