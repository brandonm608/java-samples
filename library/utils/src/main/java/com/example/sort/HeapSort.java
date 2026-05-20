package com.example.sort;

import java.util.Comparator;

public class HeapSort<T> extends BaseSort<T> {
    HeapSort(Comparator<T> comparator) {
        super(comparator);
    }

    public static <T> void sort(final T[] a, final Comparator<T> cmp) {
        final HeapSort<T> hs = new HeapSort<>(cmp);
        hs.sort(a);
    }

    @Override
    public void sort(final T[] a) {
        heapify(a);
        for (int i = a.length - 1; i >= 0; i--) {
            swapRoot(a, i);
        }
    }

    protected static int getLeftChildIndex(final int currentIndex) {
        return 2 * currentIndex + 1;
    }

    protected static int getParentIndex(final int currentIndex) {
        if (currentIndex % 2 == 0) {
            return currentIndex / 2 - 1;
        }

        return currentIndex / 2;
    }

    protected static int getRightChildIndex(final int currentIndex) {
        return 2 * currentIndex + 2;
    }

    protected void heapify(final T[] a) {
        if (a.length < 2)
            return;

        for (int i = 1; i < a.length; i++) {
            int currentIndex = i;
            int p = getParentIndex(currentIndex);
            while (p >= 0 && !isLeft(a[currentIndex], a[p])) {
                swap(a, p, currentIndex);
                currentIndex = p;
                p = getParentIndex(p);
            }
        }
    }

    protected void siftRootDown(final T[] a, final int size) {
        int currentIndex = 0;
        for (;;) {
            int l = getLeftChildIndex(currentIndex);
            int r = getRightChildIndex(currentIndex);

            if (l < size) {
                if (r < size) {
                    if (!isLeft(a[l], a[r])) {
                        if (!isLeft(a[l], a[currentIndex])) {
                            swap(a, currentIndex, l);
                            currentIndex = l;
                        } else {
                            break;
                        }
                    } else {
                        if (!isLeft(a[r], a[currentIndex])) {
                            swap(a, currentIndex, r);
                            currentIndex = r;
                        } else {
                            break;
                        }
                    }
                } else {
                    if (!isLeft(a[l], a[currentIndex])) {
                        swap(a, currentIndex, l);
                        currentIndex = l;
                    } else {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    protected void swapRoot(final T[] a, final int leafIndex) {
        swap(a, 0, leafIndex);
        siftRootDown(a, leafIndex);
    }
}
