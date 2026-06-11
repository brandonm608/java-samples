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

    void swapIfNecessary(final T[] a, final int left, final int right) {
        boolean sorted = isLeft(a[left], a[right]);
        final int swap = sorted ? left : right;
        swap(a, left, swap);
    }

    /**
     * Reverse sort array a from 0 to j. Returns True if array a is now sorted.
     *
     * @param a The array to sort.
     * @param sortedTo The index of the last identified sorted item in array a.
     * @param j The index of the end of the items to be sorted.
     * @return True if the entire array is sorted.
     */
    boolean reverseSortEnd(final T[] a, final int sortedTo, int j) {
        // If the array can not possibly be reverse sorted, short circuit. This means sortedTo must be less than j/2.
        if (sortedTo + sortedTo > j) {
            return false;
        }

        int i = 0;
        int nextI = i + 1;
        int nextJ = j - 1;

        while (i < j) {
            swapIfNecessary(a, i, j);

            if (isLeft(a[nextI], a[i]) || isLeft(a[j], a[nextJ])) {
                // Exit loop when nextI or nextJ are an out of order item.
                break;
            }

            i = nextI;
            nextI++;
            j=nextJ;
            nextJ--;
        }

        return j < i;
    }

    /**
     * Detects if array a is sorted from 0 to end or not.
     *
     * @param a The array to check if it is sorted.
     * @param end The upper bound of indexes in array a to check.
     * @return True if a is sorted.
     */
    boolean sortEnds(final T[] a, final int end) {
        int i = 0;
        int nextI = i + 1;

        while (i < end) {
            if (isLeft(a[nextI], a[i])) {
                // Exit the loop when nextI is an out of order item.
                break;
            }

            i = nextI;
            nextI++;
        }

        // Either i is not less than end and the entire array or check to see if a is reverse sorted.
        return i >= end || reverseSortEnd(a, i, end);
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
            // At this point, if there is a sorted run, there are still unsorted items. Therefore, disregarde newStart.
            hybridSort(a, 0, length - 1);
            return;
        }

        // For the sortEnds method length - 1 corresponds to the end parameter because there is no starting
        // from an offset.
        sorted = sortEnds(a, length - 1);

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
