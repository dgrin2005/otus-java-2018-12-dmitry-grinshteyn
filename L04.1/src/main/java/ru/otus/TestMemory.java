package ru.otus;

import java.util.ArrayList;
import java.util.List;

public class TestMemory {
    public void run () throws OutOfMemoryError {
        List<String> stringList = new ArrayList<>();
        final int size = 200000;
        while (true) {
            for (int i = 0; i < size; i++) {
                stringList.add("1");
            }
            System.out.println("List size " + stringList.size());
            for (int i = size / 2; i > 0; i--) {
                stringList.remove(i - 1);
            }
            System.out.println("List size " + stringList.size());
            System.out.println("Free memory " + Runtime.getRuntime().freeMemory());
            // System.gc();
        }
    }
}
