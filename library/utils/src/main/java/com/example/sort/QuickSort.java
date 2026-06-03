package com.example.sort;

import java.util.Comparator;

public class QuickSort<T> extends BaseSort<T> {
	static class MutableIntTuple {
		int first = 0;
		int second = 0;
	}

	public static <T> void sort(final T[] a, final Comparator<T> cmp) {
		QuickSort<T> qs = new QuickSort<>(cmp);
		qs.sort(a);
	}

	QuickSort(final Comparator<T> cmp) {
		super(cmp);
	}

	@Override
	public void sort(final T[] a) {
		quickSort(a, 0, a.length - 1);
	}

	private MutableIntTuple swapAdvanceBoth(final T[] a, final int left, final int right) {
		final MutableIntTuple tuple = new MutableIntTuple();
		swap(a, left, right);

		tuple.first = left + 1;
		tuple.second = right + 1;

		return tuple;
	}

	private MutableIntTuple swapNextRight(final T[] a, final int left, int right) {
		final MutableIntTuple tuple = new MutableIntTuple();
		right--;
		swap(a, left, right);

		tuple.first = left;
		tuple.second = right;

		return tuple;
	}

	private MutableIntTuple swapAdvanceLeftDecrementRight(final T[] a, final int left, final int right) {
		final MutableIntTuple tuple = new MutableIntTuple();
		swap(a, left, right);

		tuple.first = left + 1;
		tuple.second = right - 1;

		return tuple;
	}

	/**
	 * Swaps the median of the values between
	 *
	 * @param a The array to perform swapping on.
	 * @param start The start index into array a.
	 * @param mid The mid-point index into array a.
	 * @param end The end index into array a.
	 */
	void medianOf3Swap(final T[] a, final int start, final int mid, final int end) {
		boolean swap;
		int swapIndex;

		// If start is the median set swapIndex to start otherwise set swapIndex to end.
		// Setting end as the swapIndex here is the setup for the rest of the method.
		swap = (isLeft(a[mid], a[start]) && isLeft(a[start], a[end])) || (isLeft(a[end], a[start]) && isLeft(a[start], a[mid]));
		swapIndex = swap ? start : end;

		// If mid is the median set swapIndex to mid otherwise keep end as the swapIndex.
		swap = (isLeft(a[start], a[mid]) && isLeft(a[mid], a[end])) || (isLeft(a[end], a[mid]) && isLeft(a[mid], a[start]));
		swapIndex = swap ? mid : swapIndex;

		// If end is the swapIndex here it is the median, which makes the swap a no-op.
		swap(a, swapIndex, end);
	}

	MutableIntTuple partition(final T[] a, int start, int end) {
		final MutableIntTuple tuple = new MutableIntTuple();
		final T pivot;
		int unprocessed = start;
		int pivotsInsertionStart = start;
		int pivotsStart = end;

		medianOf3Swap(a, start, (end - start + 1) / 2, end);
		pivot = a[end];

		// Process items until we are just behind where pivots are being stashed. Note the pivots are thrown to the end.
		while (unprocessed < pivotsStart) {
			if (isLeft(a[unprocessed], pivot)) {
				// if unprocessed is to the left of pivot, its correct position is before the insertion point of the first pivot.

				// The unprocessed item is not permanently in its correct location and therefore now porcessed.
				final MutableIntTuple nextTuple = swapAdvanceBoth(a, pivotsInsertionStart, unprocessed);
				pivotsInsertionStart = nextTuple.first;
				unprocessed = nextTuple.second;
			} else if (isSame(a[unprocessed], pivot)) {
				// if unprocessed is the same as the pivot, unprocessed needs to be placed behind the last pivot
				// where the pivots are being stashed. Since unprocessed does not change, it will be evaluated on the next pass.

				final MutableIntTuple nextTuple = swapNextRight(a, unprocessed, pivotsStart);
				unprocessed = nextTuple.first;
				pivotsStart = nextTuple.second;
			} else {
				// If unprocessed is not to the left of the pivot and is not the same as the pivot, then it needs
				// to stay in place. It will be swapped either in the next iteration or when the pivots are reorganized.

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

	void quickSort(final T[] a, final int start, final int end) {
		if (start < end) {
			final MutableIntTuple tuple;
			final int startPivotIndex;
			final int endPivotIndex;

			tuple = partition(a, start, end);

			startPivotIndex = tuple.first;
			endPivotIndex = tuple.second;

			quickSort(a, start, startPivotIndex - 1);
			quickSort(a, endPivotIndex + 1, end);
		}
	}
}
