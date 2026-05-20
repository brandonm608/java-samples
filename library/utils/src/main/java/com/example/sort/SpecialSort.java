package com.example.sort;

import java.util.Comparator;

class SpecialSort<T> extends BaseSort<T> {
    SpecialSort(final Comparator<T> cmp) {
        super(cmp);
    }

    @Override
    public void sort(final T[] a) {
        specialSort(a, 0, a.length - 1);
    }

    public void specialSort3(final T[] sequence, final int start, final int end) {
        final int tmpIdx2 = start + 1;

        if (isLeft(sequence[start], sequence[tmpIdx2])) {
            if (isLeft(sequence[tmpIdx2], sequence[end])) {
                return;
            } else {
                if (isLeft(sequence[start], sequence[end])) {
                    swap(sequence, tmpIdx2, end);
                } else {
                    final T tmp = sequence[end];
                    sequence[end] = sequence[tmpIdx2];
                    sequence[tmpIdx2] = sequence[start];
                    sequence[start] = tmp;
                }
            }
        } else {
            if (isLeft(sequence[start], sequence[end])) {
                swap(sequence, start, tmpIdx2);
            } else if (isLeft(sequence[tmpIdx2], sequence[end])) {
                final T tmp = sequence[end];
                sequence[end] = sequence[start];
                sequence[start] = sequence[tmpIdx2];
                sequence[tmpIdx2] = tmp;
            } else {
                swap(sequence, start, end);
            }
        }
    }

    public void specialSort(final T[] a, final int start, final int end) {
        int i = start + 2;
        specialSort3(a, start, i);

        for (; i < end; i++) {
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
