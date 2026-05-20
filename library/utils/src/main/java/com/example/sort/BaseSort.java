package com.example.sort;

import java.util.Comparator;

abstract class BaseSort<T> implements Sort<T> {
    private final Comparator<T> cmp;

    public BaseSort(final Comparator<T> cmp) {
        this.cmp = cmp;
    }

    public boolean isSame(final T first, final T second) {
        return cmp.compare(first, second) == 0;
    }

    public boolean isLeft(final T first, final T second) {
        return cmp.compare(first, second) < 0;
    }

    public void swap(final T[] a, final int i, final int j) {
        T tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
}
