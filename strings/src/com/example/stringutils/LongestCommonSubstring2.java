package com.example.stringutils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Naive second attempt at a dynamic programming solution to find the longest common substring of two strings.
 * This solution is the same as the first but uses less memory by optimizing the space complexity.
 * Like the first solution, this solution has a time complexity of O(M*N) where M is the length of the first string and N is the
 * length of the second string. The space complexity is O(N) where N is the length of the second string (or the longer string if optimized).
 */
class LongestCommonSubstring2 {
    /**
     * A record to describe a run of common characters in a substring between two strings.
     * Used to return the length and endIndex of a common substring. It also provides information
     * necessary for correctly updating the `lens` array in the multi-threaded version without race conditions.
     *
     * @param length The length of the common substring found within a processed block.
     * @param endIndex The 0-based index in the second string (str2) where the common substring ends.
     * @param lastLen The calculated length of the common substring ending at `blockEnd` for the current character `ch`.
     *                This value is returned to allow the main thread to update `lens[blockEnd]` after all
     *                parallel blocks have completed, preventing race conditions.
     * @param blockEnd The 0-based index of the last element processed by this block in the `lens` array.
     *                 This indicates where `lastLen` should be placed in the `lens` array.
     */
    private record LongestRun(int length, int endIndex, int lastLen, int blockEnd) {
    }

    /**
     * Finds the longest common substring of two strings using a single-threaded dynamic programming approach.
     * This algorithm optimizes space complexity to O(N) where N is the length of the second string.
     * If multiple longest common substrings exist, this method returns the first one encountered.
     *
     * @param str1 First string to compare. Must not be null.
     * @param str2 Second string to compare. Must not be null.
     * @return A new string containing the longest common substring, or an empty string if no common substring is found.
     */
    public static String longestSubstring(final String str1, final String str2) {
        if (str1.isEmpty() || str2.isEmpty()) {
            return "";
        }

        final char[] strArray1 = str1.toCharArray();
        final char[] strArray2 = str2.toCharArray();
        final int[] lens = new int[strArray2.length];
        int longest = 0;
        int longestEndIndex = 0;

        for (char ch : strArray1) {
            int oldLen = lens[0];
            if (ch == strArray2[0]) {
                lens[0] = 1;

                if (1 > longest) {
                    longest = 1;
                    longestEndIndex = 0;
                }
            }

            for (int j = 1; j < strArray2.length; j++) {
                final int curLen = lens[j];

                if (ch == strArray2[j]) {
                    lens[j] = oldLen + 1;

                    if (lens[j] > longest) {
                        longest = lens[j];
                        longestEndIndex = j;
                    }
                } else {
                    lens[j] = 0;
                }

                oldLen = curLen;
            }
        }

        if (longest == 0) {
            return "";
        }

        // Add one here as longestEndIndex - longest will give us the index of the longestStartIndex + 1
        // and after adding 1 we can now also use it as the length of the substring
        longestEndIndex++;
        return str2.substring(longestEndIndex - longest, longestEndIndex);
    }

    /**
     * A helper method to calculate common substring lengths for a specific character `ch` within a defined block
     * of the `strArray` and `lens` array. It returns a `LongestRun` object containing the longest common
     * substring found within this block and information needed for parallel updates.
     *
     * @param strArray The character array (derived from the longer of the two input strings) to be compared. Must not be null.
     * @param lens A parallel array to `strArray` that holds the current lengths of common substrings ending at each index. Must not be null.
     * @param start The 0-based starting index (inclusive) of the block to be processed in `strArray` and `lens`.
     * @param end The 0-based ending index (exclusive) of the block to be processed in `strArray` and `lens`.
     * @param ch The character from the first string (`strArray1`) to compare against characters in `strArray`.
     * @return A `LongestRun` object representing the longest common substring found within the specified block.
     */
    private static LongestRun getLongestRun(final char[] strArray, final int[] lens, int start, int end, final char ch) {
        int longest = 0;
        int longestEndIndex = 0;

        // The original `oldLen` logic from the single-threaded version needs careful adaptation for parallel blocks.
        // `oldLen` for `lens[start]` depends on `lens[start - 1]`, which might be in a different block or not yet updated.
        // For simplicity and to avoid complex synchronization within this method, we calculate `lens[start]` based on
        // `lens[start - 1]` if `start > 0`, otherwise it's 0 or 1.
        // The `lastLen` in the returned `LongestRun` will handle the correct update of `lens[end-1]` in the main loop.

        int oldLen = lens[start];

        // Handle the first element of the block separately
        if (ch == strArray[start]) {
            if (start == 0) {
                lens[0] = 1;
            } else {
                lens[start] = lens[start - 1] + 1;
            }

            if (lens[start] > longest) {
                longest = lens[start];
                longestEndIndex = start;
            }
        } else {
            lens[start] = 0;
        }

        // We need to process strArray from start + 1 which we just processed, and go until we reach end - 1.
        start++;
        end--;

        for (int j = start; j < end; j++) {
            final int curLen = lens[j];

            if (ch == strArray[j]) {
                lens[j] = oldLen + 1;

                if (lens[j] > longest) {
                    longest = lens[j];
                    longestEndIndex = j;
                }
            } else {
                lens[j] = 0;
            }

            oldLen = curLen; // Update oldLen for the next iteration
        }

        // This needs to be processed separately from the main loop so we can ensure we do not create a race condition
        // and clobber lens[end] for the next thread.
        if (ch == strArray[end]) {
            oldLen++;

            if (oldLen > longest) {
                longest = oldLen;
                longestEndIndex = end;
            }
        } else {
            oldLen = 0;
        }

        return new LongestRun(longest, longestEndIndex, oldLen, end);
    }

    /**
     * Helper method to retrieve the `LongestRun` object from a `Future`, handling checked exceptions.
     *
     * @param future A Future object containing a `LongestRun` result. Must not be null.
     * @return The `LongestRun` object retrieved from the Future.
     * @throws RuntimeException if the computation was cancelled, threw an exception, or the current thread was interrupted.
     */
    private static LongestRun getRun(Future<LongestRun> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds the longest common substring of two strings using a multi-threaded dynamic programming approach.
     * This algorithm parallelizes the inner loop of the dynamic programming solution across multiple threads.
     * If multiple longest common substrings exist, this method returns the first one encountered.
     *
     * @param str1 First string to compare. Must not be null.
     * @param str2 Second string to compare. Must not be null.
     * @return A new string containing the longest common substring, or an empty string if no common substring is found.
     */
    public static String parallelLongestSubstring(final String str1, final String str2) {
        if (str1.isEmpty() || str2.isEmpty()) {
            return "";
        }

        /* To find the longest common substring, we take the longer string and create a parallel `lens` array to it.
           We then divide the `lens` array into blocks and process each block in parallel for each character
           of the shorter string.
           After all parallel blocks for a given character have completed, the main thread collects the results
           and correctly updates the `lens` array to avoid race conditions, and then determines if the
           overall longest common substring needs to be updated.
         */

        try (ForkJoinPool pool = ForkJoinPool.commonPool()) {
            final char[] strArray1;
            final char[] strArray2;
            final int[] lens;
            final String compStr; // The string from which the final substring will be extracted.
            final int parallelization = pool.getParallelism(); // Use getParallelism() for the actual parallelism level
            final int numBlocks;
            final int blockSize;
            final int lastBlock;
            LongestRun longestRun = new LongestRun(0, 0, 0, 0);

            // Determine which string is shorter to iterate over its characters (strArray1)
            // and which is longer to build the `lens` array (strArray2).
            if (str1.length() < str2.length()) {
                strArray1 = str1.toCharArray();
                strArray2 = str2.toCharArray();
                compStr = str2;
            } else {
                strArray1 = str2.toCharArray();
                strArray2 = str1.toCharArray();
                compStr = str1;
            }

            lens = new int[strArray2.length];

            // Determine the number of blocks for parallel processing.
            // The division by 1000 is an arbitrary heuristic to ensure a reasonable block size
            // and prevent excessive task creation for very short strings.
            numBlocks = Integer.min(parallelization, strArray2.length / 1000 + 1);
            blockSize = strArray2.length / numBlocks;
            lastBlock = numBlocks - 1;

            for (char ch : strArray1) {
                final List<Future<LongestRun>> futures = new ArrayList<>(numBlocks);
                for (int j = 0; j < numBlocks; j++) {
                    final int block = j;
                    final Future<LongestRun> future = pool.submit(() -> {
                        final int start = block * blockSize;
                        final int end;

                        if (block < lastBlock) {
                            end = start + blockSize;
                        } else {
                            end = strArray2.length; // The last block goes to the end of the array.
                        }

                        return getLongestRun(strArray2, lens, start, end, ch);
                    });

                    futures.add(future);
                }

                // Process the results from the futures.
                // Iterating backwards ensures that updates to `lens[curRun.blockEnd]` do not
                // interfere with `lens[start-1]` calculations of subsequent blocks if they were to run
                // concurrently or if the order of processing futures was not guaranteed.
                // By updating `lens[curRun.blockEnd]` in the main thread after all blocks have finished
                // for the current `ch`, we avoid race conditions.
                for (int j = futures.size() - 1; j >=0; j--) {
                    final Future<LongestRun> future = futures.get(j);
                    final LongestRun curRun = getRun(future);

                    // Update the last element of the block in the shared `lens` array.
                    // This update must happen after the previous block has completed its calculations for the current `ch`.
                    lens[curRun.blockEnd] = curRun.lastLen;

                    if (curRun.length > longestRun.length) {
                        longestRun = curRun;
                    } else if (curRun.length == longestRun.length) {
                        // If lengths are equal, prioritize the one that appears earlier in the string.
                        if (curRun.endIndex < longestRun.endIndex) {
                            longestRun = curRun;
                        }
                    }
                }
            }

            if (longestRun.length == 0) {
                return "";
            }

            pool.shutdown();

            // The substring needs to be extracted from `compStr` using the `longestRun` details.
            // `longestRun.endIndex` is 0-based, so `longestRun.endIndex + 1` gives the exclusive end for `substring()`.
            // `longestRun.endIndex - longestRun.length + 1` gives the inclusive start.
            return compStr.substring(longestRun.endIndex - longestRun.length + 1, longestRun.endIndex + 1);
        }
    }
}
