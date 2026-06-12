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
            final QuickSort.ImmutableIntTuple tuple;
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

    int swapIfNecessary(final T[] a, final int left, final int right) {
        boolean sorted = isLeft(a[left], a[right]);
        final int swap = sorted ? left : right;
        swap(a, left, swap);
        return swap;
    }

    /**
     * Reverse sort array a from 0 to j. Returns True if array a is now sorted.
     *
     * @param a The array to sort.
     * @param end The index of the end of the items to be sorted.
     * @return True if the entire array is sorted.
     */
    boolean reverseSort(final T[] a, final int end) {
        int i = 0;
        int nextI = i + 1;
        int j = end;

        while (i < j) {
            final int nextJ = swapIfNecessary(a, i, j) - 1;

            i = nextI;
            nextI++;
            j=nextJ;
        }

        // If 2i >= end the array has been sorted. If it is not sorted, check if it is sorted.
        return end - i - i <= 0 || sorted(a, end);
    }

    /**
     * Detects if array a is sorted from 0 to end or not.
     *
     * @param a The array to check if it is sorted.
     * @param end The upper bound of indexes in array a to check.
     * @return True if a is sorted.
     */
    boolean sorted(final T[] a, final int end) {
        int i = 0;
        int nextI = i + 1;

        while (i < end && isLeftOrSame(a[i], a[nextI])) {
            i = nextI;
            nextI++;
        }

        // Either i is not less than end and the entire array or check to see if a is reverse sorted.
        return i >= end;
    }

    /**
     * A modified merge sort algorithm that uses merge sort until it reaches a defined size. It then switches to
     * quicksort. Additionally, a sorted run from 0 to length - 1 is identified. If the items from 0 to length - 1
     * are reverse sorted, this is corrected and the sorted run is identified.
     *
     * @param a The array to sort.
     * @param length The length of the unsorted part of the array. In this algorithm, the array is partitioned
     *               making the length less than a.length. However, the unsorted array is from 0 to length.
     */
    protected void hybridMergeSort(final T[] a, final int length) {
        final boolean sorted;
        final int partition;
        final T[] b;

        if (length < 1024) {
            hybridSort(a, 0, length - 1);
            return;
        }

        sorted = reverseSort(a, length - 1);

        if (sorted) {
            return;
        }

        partition = length / 2;

        hybridMergeSort(a, partition);
        b = Arrays.copyOfRange(a, partition, length);
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
