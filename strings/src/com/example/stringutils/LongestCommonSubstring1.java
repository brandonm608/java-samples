package com.example.stringutils;

/**
 * Naive first attempt at a dynamic programming solution to find the longest common substring of two strings.
 * This solution uses a 2D array to store the lengths of common suffixes.
 * The time complexity of this solution is O(M*N) where M is the length of the first string and N is the length of the second string.
 * The space complexity is also O(M*N) due to the 2D `lens` array.
 */
class LongestCommonSubstring1 {
    /**
     * Finds the longest common substring of two strings using a single-threaded dynamic programming approach.
     * This algorithm has a time complexity of O(M*N) and space complexity of O(M*N).
     * If multiple longest common substrings exist, this method returns the first one encountered.
     *
     * @param str1 The first string to compare. Must not be null.
     * @param str2 The second string to compare. Must not be null.
     * @return A new string containing the longest common substring, or an empty string if no common substring is found.
     */
    public static String longestSubstring(final String str1, final String str2) {
        if (str1.isEmpty() || str2.isEmpty()) {
            return "";
        }

        final char[] strArray1 = str1.toCharArray();
        final char[] strArray2 = str2.toCharArray();
        final int[][] lens = new int[strArray1.length][strArray2.length];
        int longest = 0;
        int longestEndIndex = 0;

        for (int j = 0; j < strArray2.length; j++) {
            if (strArray1[0] == strArray2[j]) {
                lens[0][j] = 1;
                longest = 1;
                // longestEndIndex is already 0
            }
            // Otherwise lens[i][j] == 0 through initialization
        }

        for (int i = 1; i < strArray1.length; i++) {
            if (strArray1[i] == strArray2[0]) {
                lens[i][0] = 1;

                if (1 > longest) {
                    longest = 1;
                    longestEndIndex = i;
                }
            }

            for (int j = 1; j < strArray2.length; j++) {
                if (strArray1[i] == strArray2[j]) {
                    lens[i][j] = lens[i - 1][j - 1] + 1;
                    if (lens[i][j] > longest) {
                        longest = lens[i][j];
                        longestEndIndex = i;
                    }
                }
                // Otherwise lens[i][j] == 0 through initialization
            }
        }

        if (longest == 0) {
            return "";
        }

        // Add one here as longestEndIndex - longest will give us the index of the longestStartIndex + 1
        // and after adding 1 we can now also use it as the length of the substring
        longestEndIndex++;
        return str1.substring(longestEndIndex - longest, longestEndIndex);
    }
}
