package com.example.sort;

import java.util.Arrays;
import java.util.Comparator;

class MergeSortInternal<T> extends BaseSort<T> {
    MergeSortInternal(final Comparator<T> cmp) {
        super(cmp);
    }

    @Override
    public void sort(final T[] a) {
        partition(a, a.length);
    }

    public void merge(final T[] a, final T[] b, int j, final int length) {
        int i;
        int k = b.length - 1;

        // The value of j represents the index of the beginning of the sorted elements on the right side
        // of the partition. So decrement j to start at the last element on the right side of the partition
        j--;

        // Note: to ensure you do not clobber sorted values on the left side, start at the ends
        // and work backwards.
        for (i = length - 1; j >= 0 && k >= 0; i--) {
            // This logic maintains a stable sort.
            if (isLeft(b[k], a[j])) {
                // if b[k] comes before a[j], then put a[j] to the right of b[k].
                a[i] = a[j];
                j--;
            } else {
                // if b[k] is the same as a[j] or to the right of a[j], then put b[k] to the right of b[k].
                a[i] = b[k];
                k--;
            }
        }

        // Either a[i] down is already in the right order or a[i] down needs to be set to the
        // remainder of b[k] down.
        while (k >= 0) {
            a[i] = b[k];
            i--;
            k--;
        }
    }

    public void partition(final T[] a, final int length) {
        final int partition;
        final T[] b;

        if (length < 2) {
            return;
        }

        partition = length / 2;

        partition(a, partition);

        b = Arrays.copyOfRange(a, partition, length);
        partition(b, b.length);

        merge(a, b, partition, length);
    }
}
