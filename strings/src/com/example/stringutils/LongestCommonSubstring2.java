package com.example.stringutils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Naive second attempt at a dynamic programming solution to find the longest common substring of two strings.
 * This solution is the same as the first but using less memory.
 * Like the first solution, this solution is also O(M*N) where M is the length of the first string and N is the
 * length of the second string.
 */
class LongestCommonSubstring2 {
    /**
     * A record to describe a run of common characters in a substring between two strings.
     * Used to return the length and endIndex of a common substring. Also informs on where to put lastLen in the lens
     * array so there are no race conditions in the multithreaded version of finding the longest common substring.
     *
     * @param length The length of the common substring.
     * @param endIndex The endIndex of the common substring used when figuring out where the common substring ends for
     *                 the character array used.
     * @param lastLen The length of the last element processed in the lens array.
     *                Used so that parallel updates to the lens array  does not cause a race condition.
     * @param blockEnd The last index of the lens array processed so it is known where to put lastLen in the lens array.
     */
    private record LongestRun(int length, int endIndex, int lastLen, int blockEnd) {
    }

    /**
     * Find the longest common substring of two strings.
     * This algorithm always gets the first longest substring.
     *
     * @param str1 First string to compare.
     * @param str2 Second string to compare.
     * @return A new string with the longest common substring.
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
     * A helper method to update lens from start to end and return a LongestRun object.
     *
     * @param strArray A character array to be compared to the character ch.
     * @param lens A parallel array to strArray. This array always holds a current view of common substring lengths.
     * @param start The first element to be processed in strArray and lens.
     * @param end The last element to be processed in strArray and lens.
     * @param ch The character to compare to each character in strArray.
     * @return The LongestRun object that represents the longest common substring found on this run.
     */
    private static LongestRun getLongestRun(final char[] strArray, final int[] lens, int start, int end, final char ch) {
        int longest = 0;
        int longestEndIndex = 0;

        int oldLen = lens[start];

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

            oldLen = curLen;
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

    /** Helper method to reduce clutter in the code of parallelLongestSubstring.
     *
     * @param future a Future object containing a LongestRun we are waiting on.
     * @return The LongestRun object retrieved from the Future.
     */
    private static LongestRun getRun(Future<LongestRun> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Find the longest common substring of two strings using multiple threads.
     * This algorithm always gets the first longest substring.
     *
     * @param str1 First string to compare.
     * @param str2 Second string to compare.
     * @return A new string with the longest common substring.
     */
    public static String parallelLongestSubstring(final String str1, final String str2) {
        if (str1.isEmpty() || str2.isEmpty()) {
            return "";
        }

        /* To find the longest common substring we take the longest string and create a parallel lens array to that string.
           We then determine the bounds of slices of the lens array and process it in chunks.
           Once we have compared all the characters of the shorter string to the longer string and updated the lens array
           appropriately for each slice, we correct the lens array and determine if the longest common substring should
           be updated.
         */

        try (ExecutorService executor = ForkJoinPool.commonPool()) {
            final char[] strArray1;
            final char[] strArray2;
            final int[] lens;
            final String compStr;
            final int parallelization = ForkJoinPool.getCommonPoolParallelism();
            final int numBlocks;
            final int blockSize;
            final int lastBlock;
            LongestRun longestRun = new LongestRun(0, 0, 0, 0);

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

            // Note this would require tuning. To see real gains the string needs to be like 10,000+ characters long.
            // Also, real gains are not seen until the string length gets pretty ridiculous.
            // We take strArray2.length / 1,000 + 1 to make sure we get at least 1 block for small numbers less than 1,000.
            numBlocks = Integer.min(parallelization, strArray2.length / 1000 + 1);
            blockSize = strArray2.length / numBlocks;
            lastBlock = numBlocks - 1;

            for (char ch : strArray1) {
                final List<Future<LongestRun>> futures = new ArrayList<>(numBlocks);
                for (int j = 0; j < numBlocks; j++) {
                    final int block = j;
                    final Future<LongestRun> future = executor.submit(() -> {
                        final int start = block * blockSize;
                        final int end;

                        if (block < lastBlock) {
                            end = start + blockSize;
                        } else {
                            end = strArray2.length;
                        }

                        return getLongestRun(strArray2, lens, start, end, ch);
                    });

                    futures.add(future);
                }

                // process backwards to ensure there is no race condition for the element at lens[curRun.blockEnd]
                for (int j = futures.size() - 1; j >=0; j--) {
                    final Future<LongestRun> future = futures.get(j);
                    final LongestRun curRun = getRun(future);

                    lens[curRun.blockEnd] = curRun.lastLen;

                    if (curRun.length > longestRun.length) {
                        longestRun = curRun;
                    } else if (curRun.length == longestRun.length) {
                        // make sure we get the longest run appearing first
                        if (curRun.endIndex < longestRun.endIndex) {
                            longestRun = curRun;
                        }
                    }
                }
            }

            if (longestRun.length == 0) {
                return "";
            }

            executor.shutdown();

            return compStr.substring(longestRun.endIndex - longestRun.length + 1, longestRun.endIndex + 1);
        }
    }
}
