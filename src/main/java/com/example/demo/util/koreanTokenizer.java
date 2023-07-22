package com.example.demo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class koreanTokenizer {

    private static final String[] first = {"ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ" , "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};

    private static final String[] middle = {"ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅘ", "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ", "ㅣ"};

    private static final String[] last = {"", "ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ", "ㄹ", "ㄺ", "ㄻ", "ㄼ", "ㄽ", "ㄾ", "ㄿ", "ㅀ", "ㅁ", "ㅂ", "ㅄ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};

    public static String tokenize(String text) {
        List<String> list = new ArrayList<>();

        for(int i = 0; i < text.length(); i++) {
            int base = text.charAt(i);
            if(base >= 0xAC00) {
                base = base - 44032;
                char firstIndex = (char)(base / 28 / 21);
                char middleIndex = (char)(base / 28 % 21);
                char lastIndex = (char)(base % 28);

                list.add(first[firstIndex]);
                list.add(middle[middleIndex]);
                list.add(last[lastIndex]);
            } else {
                list.add(String.valueOf((char) base));
            }
        }

        return list.parallelStream().collect(Collectors.joining(""));
    }

    public static String tokenizeFirst(String text) {
        List<String> list = new ArrayList<>();

        for(int i = 0; i < text.length(); i++) {
            int base = text.charAt(i);
            if(base >= 0xAC00) {
                base = base - 44032;
                char firstIndex = (char)(base / 28 / 21);

                list.add(first[firstIndex]);
            } else {
                list.add(String.valueOf((char) base));
            }
        }

        return list.parallelStream().collect(Collectors.joining(""));
    }
}
