package com.example.test.sort;

import java.util.Arrays;
import java.util.Comparator;

public class HybridSort2<T> extends AbstractSort<T> {
	private static class MutableIntTuple {
		int first = 0;
		int second = 0;
	}

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

	private void swapAdvanceBoth(final MutableIntTuple tuple, final T[] a, final int left, final int right) {
		T tmp = a[left];
		a[left] = a[right];
		a[right] = tmp;

		tuple.first = left + 1;
		tuple.second = right + 1;
	}

	private void swapNextRight(final MutableIntTuple tuple, final T[] a, final int left, int right) {
		right--;
		T tmp = a[left];
		a[left] = a[right];
		a[right] = tmp;

		tuple.first = left;
		tuple.second = right;
	}

	private void swapAdvanceLeftDecrementRight(final MutableIntTuple tuple, final T[] a, final int left, final int right) {
		T tmp = a[left];
		a[left] = a[right];
		a[right] = tmp;

		tuple.first = left + 1;
		tuple.second = right - 1;
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

	protected boolean isEqual(final T first, final T second) {
		return compare(first, second) == 0;
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
					// Therefor end < start < mid
					// end < start && start < mid
					swap(a, end, start);
				}
				// Otherwise end is the median item.
			}
		} else {
			// mid < start
			if (isLessThan(a[start], a[end])) {
				// mid < start && start < end
				swap(a, end, start);
			} else {
				if (isLessThan(a[end], a[mid])) {
					// mid < start && end < start && end < mid
					// Therefor end < mid < start
					// end < mid && mid < start
					swap(a, end, mid);
				}
				// Otherwise end is the median item.
			}
		}
	}

	private void quickSortPartition(MutableIntTuple tuple, final T[] a, final int start, final int end) {
		final T pivot;
		int unprocessed = start;
		int pivotsInsertionStart = start;
		int pivotsStart = end;

		medianOf3Swap(a, start, (end - start + 1) / 2, end);
		pivot = a[end];

		// since we put the pivot at the end, we need to iterate unprocessed items till end - 1.
		while (unprocessed < pivotsStart) {
			if (isLessThan(a[unprocessed], pivot)) {
				swapAdvanceBoth(tuple, a, pivotsInsertionStart, unprocessed);
				pivotsInsertionStart = tuple.first;
				unprocessed = tuple.second;
			} else if (isEqual(a[unprocessed], pivot)) {
				swapNextRight(tuple, a, unprocessed, pivotsStart);
				unprocessed = tuple.first;
				pivotsStart = tuple.second;
			} else {
				// If nothing has been processed unprocessed must be in the correct location.
				unprocessed++;
			}
		}

		// reorganize pivots so they are in the center
		unprocessed = pivotsInsertionStart;
		for (int i = end; i >= pivotsStart && unprocessed < pivotsStart;) {
			swapAdvanceLeftDecrementRight(tuple, a, unprocessed, i);
			unprocessed = tuple.first;
			i = tuple.second;
		}

		tuple.first = pivotsInsertionStart;
		tuple.second = pivotsInsertionStart + end - pivotsStart;
	}

	private void quickSortInternal(final MutableIntTuple tuple, final T[] a, final int start, final int end) {
		if (start < end) {
			final int startIndex;
			final int endIndex;

			quickSortPartition(tuple, a, start, end);

			// Since tuple is reused store the state.
			startIndex = tuple.first;
			endIndex = tuple.second;

			quickSortInternal(tuple, a, start, startIndex - 1);
			quickSortInternal(tuple, a, endIndex + 1, end);
		}
	}

	protected void quickSort(final T[] a, final int start, final int end) {
		// Create a tuple object here and reuse throughout the code so we do not litter the heap with objects that need
		// to be garbage collected.
		final MutableIntTuple tuple = new MutableIntTuple();
		quickSortInternal(tuple, a, start, end);
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
