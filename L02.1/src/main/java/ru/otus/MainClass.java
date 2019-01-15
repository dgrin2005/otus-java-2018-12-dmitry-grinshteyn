package ru.otus;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) throws InterruptedException {
        MemoryStand memoryStand = new MemoryStand();
        Factory factory;

        System.out.println("Object");
        factory = new Factory(Object::new);
        memoryStand.getUsedMemory(factory);
        System.out.println();

        System.out.println("byte[1]");
        factory = new Factory(() -> new byte[1]);
        memoryStand.getUsedMemory(factory);
        System.out.println();

        System.out.println("String \"\"");
        factory = new Factory(() -> new String(""));
        memoryStand.getUsedMemory(factory);
        System.out.println();

        System.out.println("String \"123456\"");
        factory = new Factory(() -> new String("123456"));
        memoryStand.getUsedMemory(factory);
        System.out.println();

        System.out.println("TestClass");
        factory = new Factory(TestClass::new);
        memoryStand.getUsedMemory(factory);
        System.out.println();

        System.out.println("String[0]");
        factory = new Factory(() -> new String[]{});
        memoryStand.getUsedMemory(factory);
        System.out.println();

        System.out.println("String[3]");
        factory = new Factory(() -> new String[]{"A", "B", "C"});
        memoryStand.getUsedMemory(factory);
        System.out.println();

        System.out.println("String[11]");
        factory = new Factory(() -> new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"});
        memoryStand.getUsedMemory(factory);
        System.out.println();

        System.out.println("ArrayList[0]");
        factory = new Factory(ArrayList::new);
        memoryStand.getUsedMemory(factory);
        System.out.println();

        System.out.println("ArrayList[1]");
        factory = new Factory(() -> new ArrayList<String>(){{
            add("A");
        }});
        memoryStand.getUsedMemory(factory);
        System.out.println();

    }
}
