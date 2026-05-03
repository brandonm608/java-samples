package com.example.stringutils;

import java.util.function.BiFunction;

/***
 * A factory for getting the different approaches to finding the longest common substring of two strings.
 */
public class LongestCommonSubstringFactory {
    public enum ALGORITHM {
        LCS1,
        LCS2,
        LCS3
    }

    public static BiFunction<String, String, String> algorithm(final ALGORITHM algorithm) {
        return switch (algorithm) {
            case ALGORITHM.LCS1 -> LongestCommonSubstring1::longestSubstring;
            case ALGORITHM.LCS2 -> LongestCommonSubstring2::longestSubstring;
            default -> LongestCommonSubstring2::parallelLongestSubstring;
        };
    }
}
