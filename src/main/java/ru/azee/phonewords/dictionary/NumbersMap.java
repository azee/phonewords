package ru.azee.phonewords.dictionary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by azee on 24.10.16.
 */
public class NumbersMap {
    private Map<Character, Character> dict = new HashMap<>();

    public NumbersMap() {
        dict.put('0', '0');
        dict.put('1', '1');
        dict.put('A', '2');
        dict.put('B', '2');
        dict.put('C', '2');
        dict.put('D', '3');
        dict.put('E', '3');
        dict.put('F', '3');
        dict.put('G', '4');
        dict.put('H', '4');
        dict.put('I', '4');
        dict.put('J', '5');
        dict.put('K', '5');
        dict.put('L', '5');
        dict.put('M', '6');
        dict.put('N', '6');
        dict.put('O', '6');
        dict.put('P', '7');
        dict.put('Q', '7');
        dict.put('R', '7');
        dict.put('S', '7');
        dict.put('T', '8');
        dict.put('U', '8');
        dict.put('V', '8');
        dict.put('W', '9');
        dict.put('X', '9');
        dict.put('Y', '9');
        dict.put('Z', '9');
    }

    public Map<Character, Character> getDict() {
        return dict;
    }
}
