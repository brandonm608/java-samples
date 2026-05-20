package com.example.sort;

import java.util.Comparator;

public class QuickSort<T> implements Sort<T> {
	public static <T> void sort(final T[] a, final Comparator<T> cmp) {
		QuickSort<T> qs = new QuickSort<>(cmp);
		qs.sort(a);
	}

	private final QuickSortInternal<T> qs;

	QuickSort(final Comparator<T> cmp) {
		qs = new QuickSortInternal<>(cmp);
	}

	@Override
	public void sort(final T[] a) {
		qs.sort(a);
	}
}
