package com.example.test.sort;

import java.util.Arrays;
import java.util.Comparator;

public class HybridSort2<T> extends AbstractSort<T> {
	protected void specialSort3(final T[] sequence, final int start, final int end) {
		final int tmpIdx2 = start + 1;

		if (isLessThan(sequence[start], sequence[tmpIdx2])) {
			if (isLessThan(sequence[tmpIdx2], sequence[end])) {
				return;
			} else {
				if (isLessThan(sequence[start], sequence[end])) {
					swap(sequence, tmpIdx2, end);
				} else {
					final T tmp = sequence[end];
					sequence[end] = sequence[tmpIdx2];
					sequence[tmpIdx2] = sequence[start];
					sequence[start] = tmp;
				}
			}
		} else {
			if (isLessThan(sequence[start], sequence[end])) {
				swap(sequence, start, tmpIdx2);
			} else if (isLessThan(sequence[tmpIdx2], sequence[end])) {
				final T tmp = sequence[end];
				sequence[end] = sequence[start];
				sequence[start] = sequence[tmpIdx2];
				sequence[tmpIdx2] = tmp;
			} else {
				swap(sequence, start, end);
			}
		}
	}

	protected void specialSort(final T[] a, final int start, final int end) {
		int i = start + 2;
		specialSort3(a, start, i);

		for (; i < end; i++) {
			for (int k = i; k >= start; k--) {
				if (!(compare(a[k], a[k + 1]) < 0)) {
					swap(a, k, k + 1);
				} else {
					break;
				}
			}
		}
	}

	protected void swap(final T[] a, final int i, final int j) {
		final T tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	protected void merge(final T[] a, final T[] b, int j, int end) {
		int i;
		int k = b.length - 1;

		j--;
		for (i = end - 1; j >= 0 && k >= 0; i--) {
			if (!(compare(a[j], b[k]) < 0)) {
				a[i] = a[j];
				j--;
			} else {
				a[i] = b[k];
				b[k] = null;
				k--;
			}
		}

		while (k >= 0) {
			a[i] = b[k];
			i--;
			k--;
		}
	}

	protected void partition(final T[] a, final int end) {
		int partition;

		if (end < 1000) {
			hybridSort(a, 0, end - 1);
			return;
		}

		partition = end / 2;

		final T[] b = Arrays.copyOfRange(a, partition, end);

		partition(a, partition);
		partition(b, b.length);

		merge(a, b, partition, end);
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

	protected int quickSortPartition(final T[] a, final int start, final int end) {
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

	protected void quickSort(final T[] a, int start, int end) {
		if (start < end) {
			final int pivot = quickSortPartition(a, start, end);

			hybridSort(a, start, pivot - 1);
			hybridSort(a, pivot + 1, end);
		}
	}

	protected void hybridSort(final T[] a, final int start, final int end) {
		if (end - start > 8) {
			quickSort(a, start, end);
			return;
		}

		// 7 to 3 items
		if (end - start > 1) {
			specialSort(a, start, end);
			return;
		}

		// 2 items
		if (end - start > 0) {
			if (isLessThan(a[end], a[start])) {
				swap(a, start, end);
			}
		}
	}

	public HybridSort2(final Comparator<T> cmp) {
		super(cmp);
	}

	@Override
	public void sort(final T[] a) {
		partition(a, a.length);
	}
}
