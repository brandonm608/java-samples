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

	/**
	 * Swaps the median of the values between the items at a[left], a[mid], and a[right] from the index of
	 * the median item to a[right].
	 *
	 * @param a The array to perform swapping on.
	 * @param left The left index of array a to evaluate for a pivot value.
	 * @param mid The mid-point index of array a to evaluate for a pivot value.
	 * @param right The right index of array a to evaluate for a pivot and stash the pivot value.
	 */
	void medianOf3Swap(final T[] a, final int left, final int mid, final int right) {
		boolean swap;
		int swapIndex;

		// If left is the median set swapIndex to left, otherwise set swapIndex to right.
		// Setting right as the swapIndex here is the setup for the rest of the method.
		swap = (isLeft(a[mid], a[left]) && isLeft(a[left], a[right])) || (isLeft(a[right], a[left]) && isLeft(a[left], a[mid]));
		swapIndex = swap ? left : right;

		// If mid is the median set swapIndex to mid otherwise keep right as the swapIndex.
		swap = (isLeft(a[left], a[mid]) && isLeft(a[mid], a[right])) || (isLeft(a[right], a[mid]) && isLeft(a[mid], a[left]));
		swapIndex = swap ? mid : swapIndex;

		// If end is the swapIndex here it is the median, which makes the swap a no-op.
		swap(a, swapIndex, right);
	}

	MutableIntTuple partition(final T[] a, final int start, final int end) {
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
				// If unprocessed is to the left of pivot, its correct position is before the insertion point of the first pivot.
				// Since unprocessed is in the correct position, advance to the next unprocessed item.
				swap(a, unprocessed, pivotsInsertionStart);
				pivotsInsertionStart++;
				unprocessed++;
			} else if (isSame(a[unprocessed], pivot)) {
				// If unprocessed is the same as the pivot, unprocessed needs to be placed behind the last pivot.
				// This means stash it at the end behind the other pivots.
				// Keep unprocessed the same so it will be evaluated on the next pass.
				pivotsStart--;
				swap(a, unprocessed, pivotsStart);
			} else {
				// If unprocessed is not to the left of the pivot and is not the same as the pivot, then it needs
				// to stay in place. It will be swapped either in the next iteration or when the pivots are reorganized.
				unprocessed++;
			}
		}

		// reorganize pivots so they are in the center
		unprocessed = pivotsInsertionStart;
		for (int i = end; i >= pivotsStart && unprocessed < pivotsStart; i--) {
			swap(a, i, unprocessed);
			unprocessed++;
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
