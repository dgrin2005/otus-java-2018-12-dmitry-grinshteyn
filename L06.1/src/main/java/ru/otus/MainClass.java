package ru.otus;

/*
VM Options:
-Xms256m -Xmx256m
 */

import ru.otus.CacheEngine.CacheEngine;
import ru.otus.CacheEngine.CacheEngineImpl;
import ru.otus.CacheEngine.CacheException;

public class MainClass {

    public static void main(String[] args) {
        int amount = 900;

        try {
            CacheEngine cache = new CacheEngineImpl(1000);
            for (int i = 0; i < amount; i ++) {
                cache.put(i, new BigObject(String.valueOf(i)));
            }
            for (int i = 0; i < amount; i ++) {
                System.out.println(i + " - " + cache.get(i));
            }
            System.out.println("Hits = " + cache.getHitsCount());
            System.out.println("Miss = " + cache.getMissCount());

        } catch (CacheException e) {
            e.printStackTrace();
        }
    }

    static class BigObject {
        private String name;
        private byte[] array;

        public BigObject(String name) {
            this.name = name;
            array = new byte[1024 * 1024];
        }

        @Override
        public String toString() {
            return "BigObject{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
