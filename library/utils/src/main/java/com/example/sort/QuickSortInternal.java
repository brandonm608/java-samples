package com.example.sort;

import java.util.Comparator;

class QuickSortInternal<T> extends BaseSort<T> {
    public static class MutableIntTuple {
        int first = 0;
        int second = 0;
    }

    QuickSortInternal(final Comparator<T> cmp) {
        super(cmp);
    }

    @Override
    public void sort(final T[] a) {
        quickSort(a, 0, a.length - 1);
    }

    public MutableIntTuple swapAdvanceBoth(final T[] a, final int left, final int right) {
        final MutableIntTuple tuple = new MutableIntTuple();
        swap(a, left, right);

        tuple.first = left + 1;
        tuple.second = right + 1;

        return tuple;
    }

    public MutableIntTuple swapNextRight(final T[] a, final int left, int right) {
        final MutableIntTuple tuple = new MutableIntTuple();
        right--;
        swap(a, left, right);

        tuple.first = left;
        tuple.second = right;

        return tuple;
    }

    public MutableIntTuple swapAdvanceLeftDecrementRight(final T[] a, final int left, final int right) {
        final MutableIntTuple tuple = new MutableIntTuple();
        swap(a, left, right);

        tuple.first = left + 1;
        tuple.second = right - 1;

        return tuple;
    }

    public void medianOf3Swap(final T[] a, final int start, final int mid, final int end) {
		/*
		3 cases to consider.
		start < mid && mid < end || end < mid && mid < start
		mid < start && start < end || end < start && start < mid
		start < end && end < mid || mid < end && end < start
		*/

        if (isLeft(a[start], a[mid])) {
            // start < mid
            if (isLeft(a[mid], a[end])) {
                // start < mid && mid < end
                swap(a, end, mid);
            } else {
                // start < mid && end < mid
                if (isLeft(a[end], a[start])) {
                    // start < mid && end < mid && end < start
                    // Therefor, end < start < mid
                    // end < start && start < mid
                    swap(a, end, start);
                }
                // Otherwise end is the median item.
            }
        } else {
            // mid < start
            if (isLeft(a[start], a[end])) {
                // mid < start && start < end
                swap(a, end, start);
            } else {
                if (isLeft(a[end], a[mid])) {
                    // mid < start && end < start && end < mid
                    // Therefor, end < mid < start
                    // end < mid && mid < start
                    swap(a, end, mid);
                }
                // Otherwise end is the median item.
            }
        }
    }

    public MutableIntTuple partition(final T[] a, int start, int end) {
        final MutableIntTuple tuple = new MutableIntTuple();
        final T pivot;
        int unprocessed = start;
        int pivotsInsertionStart = start;
        int pivotsStart = end;

        medianOf3Swap(a, start, (end - start + 1) / 2, end);
        pivot = a[end];

        // since we put the pivot at the end, we need to iterate unprocessed items till end - 1.
        while (unprocessed < pivotsStart) {
            if (isLeft(a[unprocessed], pivot)) {
                final MutableIntTuple nextTuple = swapAdvanceBoth(a, pivotsInsertionStart, unprocessed);
                pivotsInsertionStart = nextTuple.first;
                unprocessed = nextTuple.second;
            } else if (isSame(a[unprocessed], pivot)) {
                final MutableIntTuple nextTuple = swapNextRight(a, unprocessed, pivotsStart);
                unprocessed = nextTuple.first;
                pivotsStart = nextTuple.second;
            } else {
                // If nothing has been processed unprocessed must be in the correct location.
                unprocessed++;
            }
        }

        // reorganize pivots so they are in the center
        unprocessed = pivotsInsertionStart;
        for (int i = end; i >= pivotsStart && unprocessed < pivotsStart;) {
            final MutableIntTuple nextTuple = swapAdvanceLeftDecrementRight(a, unprocessed, i);
            unprocessed = nextTuple.first;
            i = nextTuple.second;
        }

        tuple.first = pivotsInsertionStart;
        tuple.second = pivotsInsertionStart + end - pivotsStart;

        return tuple;
    }

    public MutableIntTuple quickSort(final T[] a, final int start, final int end) {
        if (start < end) {
            final MutableIntTuple tuple;
            final int startIndex;
            final int endIndex;

            tuple = partition(a, start, end);

            startIndex = tuple.first;
            endIndex = tuple.second;

            quickSort(a, start, startIndex - 1);
            quickSort(a, endIndex + 1, end);

            return tuple;
        }

        return null;
    }
}
