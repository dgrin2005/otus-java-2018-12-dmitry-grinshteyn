package ru.otus;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.commons.lang3.CharSet;

public class AdditionalClass {

    public void createCharSet(String string) {
        CharSet charSet = CharSet.getInstance(string);
        System.out.println("String '" + string + "' contains set of chars:" + charSet);
    }

    public void createMultiset(String string) {
        Multiset<Character> multiset = HashMultiset.create();
        for (int i = 0; i < string.length(); i++) {
            multiset.add(string.charAt(i));
        }
        System.out.println("String '" + string + "' contains set of chars:" + multiset);
    }
}
