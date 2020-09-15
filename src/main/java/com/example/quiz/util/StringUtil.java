package com.example.quiz.util;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StringUtil {

    public static boolean verifyMaxChars(String str, int maxChars) {
        return str.getBytes().length <= maxChars;
    }

    public static boolean verifyMinChars(String str, int minChars) {
        return str.getBytes().length >= minChars;
    }

    public static String generateStrSpecifiedLength(int byteSize) {
        return IntStream.range(0, byteSize).mapToObj(i -> "a").collect(Collectors.joining());
    }

}
