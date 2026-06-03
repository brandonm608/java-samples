package com.example;

import com.example.stringutils.LongestCommonSubstringFactory;

import java.util.function.BiFunction;

public class LongestCommonSubstringTest {
    private static final BiFunction<String, String, String> lcsFunc1 = LongestCommonSubstringFactory.algorithm(LongestCommonSubstringFactory.ALGORITHM.LCS2);
    private static final BiFunction<String, String, String> lcsFunc2 = LongestCommonSubstringFactory.algorithm(LongestCommonSubstringFactory.ALGORITHM.LCS3);

    private static String lcs1(final String str1, final String str2) {
        return lcsFunc1.apply(str1, str2);
    }

    private static String lcs2(final String str1, final String str2) {
        return lcsFunc2.apply(str1, str2);
    }

    private static void outputlcs1lcs2(final String str1, final String str2, final String expectedResult) {
        long[] sample = new long[3];
        final String result1;
        final String result2;
        boolean isExpected = false;

        for (int i = 0; i < 3; i++) {
            final long t1, t2, t3;

            t1 = System.nanoTime();
            lcs1(str1, str1);
            t2 = System.nanoTime();
            lcs2(str1, str2);
            t3 = System.nanoTime();

            sample[i] = (t2 - t1) - (t3 - t2);
        }

        result1 = lcs1(str1, str2);
        result2 = lcs2(str1, str2);

        System.out.println("Whose faster? lcs1() - lcs2() (positive lcs2 won negative lcs1 won): " + sample[0] + " ns, " + sample[1] + " ns, " + sample[2] + " ns");
        System.out.println("Compare: '" + str1 + "' to '" + str2 + "'");
        if (!result1.equals(result2)) {
            System.out.println("lcs1 and lcs2 do not match");
            System.out.println("'" + result1 + "': lcs1");
            System.out.println("'" + result2 + "': lcs2");
        } else {
            System.out.println("lcs1 matches lcs2 and the result is: '" + result1 + "'");

            isExpected = result1.equals(expectedResult);

            System.out.print("The result ");
            if (isExpected) {
                System.out.print("matches ");
            } else {
                System.out.print("DOES NOT match ");
            }

            System.out.println("the expected result: '" + expectedResult + "'\n");
        }

        if (!isExpected) {
            throw  new RuntimeException("lcs results do not match what is expected!");
        }
    }

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            final char rand = (char) (Math.random() * 26 + 97);
            sb.append(rand);
        }

        return  sb.toString();
    }

    public static void compareReallyLongStrings(final String str1, final String str2) {
        boolean matches = true;

        for (int i = 0; i < 3; i++) {
            final long t1, t2, t3;
            final String result1, result2;
            final long sample, delta1, delta2;

            t1 = System.nanoTime();
            result1 = lcs1(str1, str2);
            t2 = System.nanoTime();
            result2 = lcs2(str1, str2);
            t3 = System.nanoTime();

            delta1 = (t2 - t1);
            delta2 = (t3 - t2);
            sample = delta1 - delta2;
            matches = matches && result1.equals(result2);
            System.out.println("Run " + (i + 1));
            System.out.println("lcs1 took " + delta1 + " ns");
            System.out.println("lcs2 took " + delta2 + " ns");
            System.out.println("Whose faster? lcs1() - lcs2() (positive lcs2 won negative lcs1 won): " + sample + " ns");
            System.out.println("Result 1 length: " + result1.length() + ", Result 2 length: " + result2.length());
        }

        System.out.println("Long strings match?: " + matches);

        if (!matches) {
            throw new RuntimeException("lcs1 and lcs2 do not match!");
        }
    }

    public static void compareAlgorithmsForReallyLongStrings() {
        String str1 = generateRandomString(10000);
        String str2 = generateRandomString(10000);

        compareReallyLongStrings(str1, str1);
        System.out.println();
        compareReallyLongStrings(str1, str2);
        System.out.println();
        str1 = generateRandomString(10000);
        str2 = generateRandomString(10000);
        compareReallyLongStrings(str1, str1);
        System.out.println();
        compareReallyLongStrings(str1, str2);
    }

    public static void main(String[] args) {
        System.out.println("This is the first cold run of lcs1 and lcs2 algorithms. Remaining runs should be faster as the code will be hot.");
        outputlcs1lcs2("aldfskjads;lkfj", "adslfkjadslkfjad", "kjads");

        outputlcs1lcs2("banana", "ogrbananaasfd", "banana");
        outputlcs1lcs2("babababaabababa", "bababaaba", "bababaaba");
        outputlcs1lcs2("babababaabababa", "nannbaana", "baa");
        outputlcs1lcs2("bababnbaabababa", "nannbaana", "nbaa");
        outputlcs1lcs2("nananafadfadfadsfnadfnadf", "oiqewuroiweqruoieafadfadfadsfnoieqwrioeqru", "afadfadfadsfn");
        outputlcs1lcs2("", "", "");
        outputlcs1lcs2("ba", "ba", "ba");
        outputlcs1lcs2("a", "a", "a");
        outputlcs1lcs2("aafds", "qewrio", "");

        compareAlgorithmsForReallyLongStrings();
    }
}
