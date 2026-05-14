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

	protected void swapStartLessThanMid(final T[] a, final int start, final int mid, final int end) {
		if (isLessThan(a[start], a[end])) {
			if (isLessThan(a[mid], a[end])) {
				swap(a, start, mid);
			} else {
				swap(a, start, end);
			}
		} else {
			if (!isLessThan(a[start], a[mid])) {
				swap(a, start, mid);
			}
		}
	}

	protected void swapStartNotLessThanMid(final T[] a, final int start, final int mid, final int end) {
		if (isLessThan(a[mid], a[end])) {
			if (!isLessThan(a[start], a[end])) {
				swap(a, start, end);
			}
		} else {
			swap(a, start, mid);
		}
	}

	protected void medianOf3Swap(final T[] a, final int start, final int mid, final int end) {
		if (isLessThan(a[start], a[mid])) {
			swapStartLessThanMid(a, start, mid, end);
		} else {
			swapStartNotLessThanMid(a, start, mid, end);
		}
	}

	private int partition(T[] a, final int start, final int end) {
		final T pivot;
		int i = start;
		int j = end;

		medianOf3Swap(a, start, (end - start) / 2, end);

		pivot = a[start];
		// pivot starts out on the right side of the array.
		i++;

		for (; i < j; j--) {
			if (isLessThan(a[j], pivot)) {
				swap(a, i, j);
				i++;
			}
		}

		// put the pivot variable where it rightly belongs.
		swap(a, start, i);
		return i;
	}

	protected void quickSort(T[] a, final int start, final int end) {
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
