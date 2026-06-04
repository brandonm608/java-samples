package com.example.sort;

import java.util.Comparator;

class InsertionSort<T> extends BaseSort<T> {
    InsertionSort(final Comparator<T> cmp) {
        super(cmp);
    }

    @Override
    public void sort(final T[] a) {
        insertionSort(a, 0, a.length - 1);
    }

    public void insertionSort(final T[] a, final int start, final int end) {
        // Protect main loop against not having enough elements to sort. The minimum is 2 elements.
        if (end - start < 1) {
            return;
        }

        for (int i = start; i < end; i++) {
            for (int k = i; k >= start; k--) {
                if (isLeft(a[k + 1], a[k])) {
                    swap(a, k, k + 1);
                } else {
                    break;
                }
            }
        }
    }
}
