package com.example.sort;

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

    void swapIfNecessary(final T[] a, final int left, final int right) {
        boolean reversed = isLeft(a[right], a[left]);
        final int swap = reversed ? right : left;
        swap(a, left, swap);
    }

    int sortEnds(final T[] a, int start, int end) {
        swapIfNecessary(a, start, end);

        while (start < end) {
            final int nextStart = start + 1;
            final int nextEnd = end - 1;
            swapIfNecessary(a, nextStart, nextEnd);

            if (!isLeftOrSame(a[start], a[nextStart])) {
                break;
            }

            start = nextStart;
            end = nextEnd;
        }

        return start < end ? start : -1;
    }

    @SuppressWarnings("unchecked")
    protected void hybridMergeSort(final T[] a, final int length) {
        final int partition;
        final int newStart;
        T[] b;

        if (length < 1024) {
            hybridSort(a, 0, length - 1);
            return;
        }

        newStart = sortEnds(a, 0, length - 1);

        if (newStart < 0 ) {
            return;
        }

        partition = length / 2;

        // Reuse array to keep from polluting the heap and causing a lot of garbage collection.
        b = (T[]) new Object[length - partition];
        if (newStart < 8) {
            hybridMergeSort(a, partition);
        } else {
            System.arraycopy(a, newStart, b, 0, partition - newStart + 1);
            ms.merge(a, b, partition, length - newStart);
        }

        System.arraycopy(a, partition, b, 0, length - partition);
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
