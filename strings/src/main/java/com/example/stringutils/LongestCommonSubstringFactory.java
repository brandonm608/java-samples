package com.example.stringutils;

import java.util.function.BiFunction;

/***
 * A factory for getting the different approaches to finding the longest common substring of two strings.
 */
public class LongestCommonSubstringFactory {
    /**
     * Enum for the different algorithms to find the longest common substring.
     */
    public enum ALGORITHM {
        /**
         * Represents the LongestCommonSubstring1 single threaded 2D array dynamic programming algorithm.
         */
        LCS1,
        /**
         * Represents the LongestCommonSubstring2 single threaded 1D array dynamic programming algorithm.
         */
        LCS2,
        /**
         * Represents the LongestCommonSubstring2 multithreaded 1D array dynamic programming algorithm.
         */
        LCS3
    }

    /**
     * Returns a BiFunction that finds the longest common substring of two strings using the specified algorithm.
     *
     * @param algorithm The algorithm to use.
     * @return A BiFunction that takes two strings and returns their longest common substring.
     */
    public static BiFunction<String, String, String> algorithm(final ALGORITHM algorithm) {
        return switch (algorithm) {
            case ALGORITHM.LCS1 -> LongestCommonSubstring1::longestSubstring;
            case ALGORITHM.LCS2 -> LongestCommonSubstring2::longestSubstring;
            default -> LongestCommonSubstring2::parallelLongestSubstring;
        };
    }
}
