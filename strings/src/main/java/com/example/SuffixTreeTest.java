package com.example;

import com.example.stringutils.SuffixTrie;

public class SuffixTreeTest {
    private static void printSuffixes(final String str) {
        System.out.println("Suffixes for string '" + str + "'");
        final SuffixTrie trie = new SuffixTrie(str);

        trie.printTrie();

        System.out.println();
    }

    private static void testContainsAndSuffix(final String str, final String compare, final boolean expectedContains, final boolean expectedSuffix) {
        final SuffixTrie trie = new SuffixTrie(str);
        final boolean contains = trie.contains(compare);
        final boolean suffix = trie.isSuffix(compare);

        System.out.println("String: '" + str + "' \nString to compare: '" + compare + "'");

        if (contains == expectedContains) {
            final String result = contains ? "does" : "does not";
            System.out.println("The string " + result + " contain the string to compare.");
        } else {
            throw new RuntimeException("String does not have the expected value of contains for the string to compare!");
        }

        if (suffix == expectedSuffix) {
            final String result = suffix ? "does" : "does not";
            System.out.println("The string " + result + " have the suffix of the string to compare.");
        } else {
            throw new RuntimeException("String does not have the expected value of suffix for the string to compare!");
        }

        if (suffix && !contains) {
            throw new RuntimeException("Something has gone horribly wrong as the string cannot have a suffix of the"
                    + " string to compare and yet not contain the string to compare!");
        }

        System.out.println();
    }

    public static void main(String[] args) {
        printSuffixes("banana");
        printSuffixes("bananacbadv");
        printSuffixes("bananadvacbadv");
        printSuffixes("bananadvacbadvna");

        testContainsAndSuffix("banana", "bananacbadv", false, false);
        testContainsAndSuffix("bananacbadv", "banana", true, false);
        testContainsAndSuffix("bananadvacbadvna", "vacba", true, false);
        testContainsAndSuffix("bananadvacbadvna", "dvna", true, true);
        testContainsAndSuffix("bananadvacbadvna", "weioqwr", false, false);
    }
}
