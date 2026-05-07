package com.example.stringutils;

import java.util.HashMap;
import java.util.Map;

/**
 * A non-compressed suffix trie implementation for string analysis. The string should not contain the character '\0'.
 */
public class SuffixTrie {
    /**
     * A node within the suffix trie.
     *
     * @param ch       The character stored at this node.
     * @param children A map of child nodes indexed by character.
     */
    private record TrieNode(char ch, Map<Character, TrieNode> children){}

    /**
     * Represents the end of a string within the trie.
     */
    private static final char END_CH = '\0';

    /**
     * The node used to represent the termination of a suffix.
     */
    private static final TrieNode END_NODE = null;

    /**
     * The root node of the suffix trie.
     */
    private final TrieNode root = new TrieNode('\0', new HashMap<>());

    /**
     * The original string for which the suffix trie was built.
     */
    private final String str;

    /**
     * Currently {@code curNode} is only used to allow {@link #isSuffix(String)} to receive the last node
     * processed by {@link #indexOfStrStartingFrom(TrieNode, String)}.
     * Could potentially be used in the future if operations are added that need to maintain state between method calls.
     */
    private TrieNode curNode = root;

    /**
     * Constructs a SuffixTrie for the given string and populates it with all suffixes.
     * Creating a SuffixTrie takes O(n^2).
     *
     * @param str The string for which to build the suffix trie. Must not be null.
     */
    public SuffixTrie(final String str) {
        this.str = str;
        fillTrie(str.toCharArray());
    }

    /**
     * Returns the original string used to construct this SuffixTrie.
     *
     * @return The original string.
     */
    public String getString() {
        return this.str;
    }

    /**
     * Fills the trie with all suffixes of the provided character array.
     *
     * @param str The character array to process. Must not be null.
     */
    private void fillTrie(final char[] str) {
        if (str.length == 0) {
            root.children.put(END_CH, END_NODE);
            return;
        }

        for (int i = str.length - 1; i >= 0; i--) {
            TrieNode curNode = root;

            for (int j = i; j < str.length; j++) {
                if (curNode.children.containsKey(str[j])) {
                    curNode = curNode.children.get(str[j]);
                } else {
                    TrieNode newNode = new TrieNode(str[j], new HashMap<>());
                    curNode.children.put(str[j], newNode);
                    curNode = newNode;
                }
            }

            curNode.children.put(END_CH, END_NODE);
        }
    }

    /**
     * Traverses the trie starting from the specified node to find the given string.
     * Sets {@code curNode} to the last node reached during the search.
     *
     * @param start The node to start the search from. Must not be null.
     * @param str   The string to search for. Must not be null.
     * @return The index reached in the search string (equal to length if found), or -1 if not found.
     */
    private int indexOfStrStartingFrom(final TrieNode start, final String str) {
        int index;

        curNode = start;

        for (index = 0; index < str.length(); index++) {
            final char ch = str.charAt(index);

            if (curNode.children.containsKey(ch)) {
                curNode = curNode.children.get(ch);
            } else {
                return -1;
            }
        }

        return index;
    }

    /**
     * Checks if the given string is contained as a substring within the original string.
     *
     * @param str The string to check for containment. Must not be null.
     * @return true if the string is contained, false otherwise.
     */
    public boolean contains(String str) {
        // This method is meant to always start searching from root.
        final int index = indexOfStrStartingFrom(root, str);

        // allow for future implementations to use curNode between method calls by making it so we always reset
        // curNode to root when using this method.
        curNode = root;

        return index > -1;
    }

    /**
     * Checks if the given string is a suffix of the original string.
     *
     * @param str The string to check if it's a suffix. Must not be null.
     * @return true if the string is a suffix, false otherwise.
     */
    public boolean isSuffix(String str) {
        // This method is meant to always start searching from root.
        final int index = indexOfStrStartingFrom(root, str);
        final boolean suffix = index > -1 && curNode.children.containsKey(END_CH);

        // allow for future implementations to use curNode between method calls by making it so we always reset
        // curNode to root when using this method.
        curNode = root;

        return suffix;
    }

    /**
     * Recursively traverses and prints suffixes starting from the given node.
     *
     * @param node   The current node in the traversal. Must not be null.
     * @param prefix The accumulated string representation of the path to this node. Must not be null.
     */
    private static void printSuffixes(final TrieNode node, final String prefix) {
        for (TrieNode child : node.children.values()) {
            if (child == END_NODE) {
                if (node.children.size() < 2) {
                    System.out.println(prefix + "$'");
                }
            } else {
                final String curStr;

                if (node.children.containsKey(END_CH)) {
                    curStr = prefix + '$' + child.ch;
                } else {
                    if (node.children.size() > 1) {
                        curStr = prefix + "->" + child.ch;
                    } else {
                        curStr = prefix + child.ch;
                    }
                }

                printSuffixes(child, curStr);
            }
        }
    }

    /**
     * Prints the suffix trie structure to the console. Primarily for debugging and visualization.
     */
    public void printTrie() {
        System.out.println("Trie: ");
        for (TrieNode node : root.children.values()) {
            printSuffixes(node, "\t'" + node.ch);
        }
    }
}
