# Library Project

This is a rollup of various code I have written. The project was started in May 2026 mostly to move from standalone
Java projects to a Gradle build that includes JUnit testing. Currently, the only package is com.example.sort.

## com.example.sort

These are implementations of various sorting algorithms I created in 2019. As of May 2026 I have refactored and modified
these algorithms heavily. If there is any interest in looking back at how they have been modified, the original
algorithms were in a simple Java project without Gradle call sorting. You will find the code under that directory
in the git history.

### Sort
This is a Java Interface all sorting algorithms implement that require one method called sort.

### BaseSort
These are a collection of common methods used in the different sorting algorithms. It is an Abstract Class that is
package private.

### HeapSort
An implementation of HeapSort using the array it is given.

It is a Java Object that extends BaseSort. As an Object it has a sort method. There is also a class static convince
method for sorting.

### HybridSort2
This is a heuristic sorting algorithm that combines MergeSort, QuickSort, and SpecialSort.

It is a Java Object that extends BaseSort. As an Object it has a sort method. There is also a class static convince
method for sorting.

This is called HybridSort2 because there was originally a HybridSort and I wanted to compare the 2. HybridSort was
broken, so I plan to resurrect it at a later date for comparison.

### MergeSort
This is a wrapper class around MergeSortInternal.

It is a Java Object that extends BaseSort. As an Object it has a sort method. There is also a class static convince
method for sorting.

### MergeSortInternal
This is the common merge sort algorithm with a twist. The twist is only on side of an array is created. This is the
only stable sort algorithm here.

It is a Java Object that extends BaseSort. As an Object it has a sort method.

The reasoning behind creating this package private class is to separate out common logic for reuse in MergeSort and
HybridSort2.

### QuickSort
This is a wrapper class around QuickSortInternal.

### QuickSortInternal
This is the common quick sort algorithm with a twist. The twist is every thing equal to the pivot ends up in the center
before returning the beginning and ending indexes of the pivots for partitioning.

It is a Java Object that extends BaseSort. As an Object it has a sort method.

The reasoning behind creating this package private class is to separate out common logic for reuse in MergeSort and
HybridSort2.

### SpecialSort

This is a collection of 2 simple sorting algorithms currently used only by HybridSort2.

It is a Java Object that extends BaseSort. As an Object it has a sort method.
