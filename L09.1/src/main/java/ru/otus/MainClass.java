package ru.otus;

import com.google.gson.Gson;

import java.util.*;

public class MainClass {

    public static void main(String[] args) throws IllegalAccessException {

        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(10);
        arrayList.add(20);
        arrayList.add(30);
        arrayList.add(40);
        arrayList.add(50);
        testJSONObjectWriter(arrayList);

        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.add(10);
        linkedList.add(20);
        linkedList.add(30);
        linkedList.add(40);
        linkedList.add(50);
        testJSONObjectWriter(linkedList);

        HashSet<Integer> hashSet = new HashSet<>();
        hashSet.add(10);
        hashSet.add(20);
        hashSet.add(30);
        hashSet.add(40);
        hashSet.add(50);
        testJSONObjectWriter(hashSet);

        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(10);
        priorityQueue.add(20);
        priorityQueue.add(30);
        priorityQueue.add(40);
        priorityQueue.add(50);
        testJSONObjectWriter(priorityQueue);

        HashMap<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(1, 10);
        hashMap.put(2, 20);
        hashMap.put(3, 30);
        hashMap.put(4, 40);
        hashMap.put(5, 50);
        testJSONObjectWriter(hashMap);


        Integer arr[] = new Integer[3];
        arr[0] = 10;
        arr[1] = 20;
        arr[2] = 30;
        testJSONObjectWriter(arr);

        Map<String, InnerTestClass> innerTestClassDoubleMap = new HashMap<>();
        innerTestClassDoubleMap.put("A", new InnerTestClass(false, "1", 1.));
        innerTestClassDoubleMap.put("B", new InnerTestClass(true,"2", 2.));
        TestClass testClass = new TestClass(
                123,
                "123",
                new InnerTestClass(true, "456", 10.),
                innerTestClassDoubleMap);
        testJSONObjectWriter(testClass);

        InnerTestClass innerTestClassArray[] = new InnerTestClass[3];
        innerTestClassArray[0] = new InnerTestClass(true, "aaa", 1.);
        innerTestClassArray[1] = new InnerTestClass(false, "bbb", 2.);
        innerTestClassArray[2] = new InnerTestClass(true, "ccc", 3.);
        testJSONObjectWriter(innerTestClassArray);

        HashSet<InnerTestClass> innerTestClassHashSet = new HashSet<>();
        innerTestClassHashSet.add(new InnerTestClass(true, "aaa", 1.));
        innerTestClassHashSet.add(new InnerTestClass(false, "bbb", 2.));
        innerTestClassHashSet.add(new InnerTestClass(true, "ccc", 3.));
        testJSONObjectWriter(innerTestClassHashSet);
    }

    static void testJSONObjectWriter(Object object) throws IllegalAccessException {
        Gson gson = new Gson();
        JSONObjectWriter jsonObjectWriter = new JSONObjectWriter();
        System.out.println(object.getClass());
        System.out.println("JSON ObjectWriter : " + jsonObjectWriter.writeToJSON(object));
        System.out.println("GSON              : " + gson.toJson(object));
        System.out.println();
    }
}
