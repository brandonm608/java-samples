package com.example.test.sort;

import java.util.Comparator;

public class QuickSort<T> extends AbstractSort<T> {
	protected void swap(final T[] a, final int i, final int j) {
		T tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	protected boolean isLessThan(final T first, final T second) {
		return compare(first, second) < 0;
	}

	protected boolean isLessThanEqual(final T first, final T second) {
		return compare(first, second) <= 0;
	}

	protected void medianOf3Swap(final T[] a, final int start, final int mid, final int end) {
		/*
		3 cases to consider.
		start < mid && mid < end || end < mid && mid < start
		mid < start && start < end || end < start && start < mid
		start < end && end < mid || mid < end && end < start
		*/

		if (isLessThan(a[start], a[mid])) {
			// start < mid
			if (isLessThan(a[mid], a[end])) {
				// start < mid && mid < end
				swap(a, end, mid);
			} else {
				// start < mid && end < mid
				if (isLessThan(a[end], a[start])) {
					// start < mid && end < mid && end < start
					swap(a, end, start);
				}
				// Otherwise end is the median item and in the correct place.
			}
		} else {
			// mid < start
			if (isLessThan(a[start], a[end])) {
				// mid < start && start < end
				swap(a, end, start);
			} else {
				if (isLessThan(a[end], a[mid])) {
					// mid < start && end < start && end < mid
					swap(a, end, mid);
				}
				// Otherwise end is the median item and in the correct place.
			}
		}
	}

	private int partition(T[] a, final int start, final int end) {
		final T pivot;
		int i = start;

		medianOf3Swap(a, start, (end - start + 1) / 2, end);
		pivot = a[end];

		// since we put the pivot at the end, we need to iterate j till end - 1.
		for (int j = start; j < end; j++) {
			if (isLessThanEqual(a[j], pivot)) {
				swap(a, i, j);
				i++;
			}
		}

		// put the pivot variable where it rightly belongs. Which is right at i. The value of i being the last
		// index on the left side of the array not less than or equal to the pivot.
		swap(a, end, i);
		return i;
	}

	protected void quickSort(T[] a, int start, int end) {
		if (start < end) {
			final int pivot = partition(a, start, end);

			quickSort(a, start, pivot - 1);
			quickSort(a, pivot + 1, end);
		}
	}

	public QuickSort(Comparator<T> cmp) {
		super(cmp);
	}

	@Override
	public void sort(T[] a) {
		quickSort(a, 0, a.length - 1);
	}
}
