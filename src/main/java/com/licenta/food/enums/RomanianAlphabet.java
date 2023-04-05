package com.licenta.food.enums;

import com.licenta.food.utils.CustomPair;

import java.util.Arrays;
import java.util.List;

public enum RomanianAlphabet {
    ă("a"),
    â("a"),
    î("i"),
    ț("t"),
    ș("s");

    RomanianAlphabet(String character) {
        this.character = character;
    }

    private final String character;

    public static String changedWord(String word) {

        List<CustomPair> characterPairs = Arrays.stream(values()).map(c -> new CustomPair(c.name(), c.character)).toList();

        for (CustomPair pair: characterPairs) {
            if (word.contains(pair.getFirst())) {
                word = word.replace(pair.getFirst(), pair.getSecond());
            }
        }

        return word;
    }
}