package ru.otus;

import java.util.ArrayList;
import java.util.Comparator;

public class MainClass {

    public static void main(String[] args) {

        MyArrayList<String> myArrayList1 = new MyArrayList<>();
        myArrayList1.add("D");
        myArrayList1.add("C");
        myArrayList1.add("B");
        myArrayList1.add("E");
        myArrayList1.add("A");
        System.out.println("New array  <String>: " + myArrayList1);

        MyArrayList.sort(myArrayList1, Comparator.comparing(String::toString));
        System.out.println("Sorted array: " + myArrayList1);

        myArrayList1.remove(2);
        System.out.println("Removed element (index 2): " + myArrayList1);

        myArrayList1.remove("A");
        System.out.println("Removed element (\"A\"): " + myArrayList1);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("X");
        arrayList.add("Y");
        arrayList.add("Z");
        myArrayList1.addAll(2, arrayList);
        System.out.println("Added collection [X, Y, Z] to position 2: " + myArrayList1);

        arrayList.remove("Y");
        myArrayList1.removeAll(arrayList);
        System.out.println("Removed collection [X, Z]: " + myArrayList1);

        arrayList.clear();
        arrayList.add("E");
        arrayList.add("B");
        myArrayList1.retainAll(arrayList);  //некорректно, меняется порядок
        System.out.println("Retained collection [E, B]: " + myArrayList1);

        myArrayList1.add("E");
        System.out.println("Array: " + myArrayList1);
        System.out.println("Index of \"E\": " + myArrayList1.indexOf("E"));
        System.out.println("Last index of \"E\": " + myArrayList1.lastIndexOf("E"));
        System.out.println("Index of \"A\": " + myArrayList1.indexOf("A"));

        myArrayList1.addAll(arrayList, "1", "2", "3");
        System.out.println("Added collection [E, B] and elements 1, 2, 3: " + myArrayList1);

        MyArrayList<String> myArrayList2 = new MyArrayList<>();
        MyArrayList.copy(myArrayList2, myArrayList1);
        System.out.println("Copied array: " + myArrayList2);

        myArrayList1.clear();
        System.out.println("Cleared array: " + myArrayList1);

        MyArrayList<Integer> myArrayList3 = new MyArrayList<>();
        myArrayList3.add(22);
        myArrayList3.add(24);
        myArrayList3.add(14);
        myArrayList3.add(57);
        myArrayList3.add(8);
        System.out.println("New array <Integer>: " + myArrayList3);

        MyArrayList.sort(myArrayList3, (o1, o2) -> (o1 - o2));
        System.out.println("Sorted array: " + myArrayList3);
        System.out.println("Sublist (2, 4): " + myArrayList3.subList(2, 4));

    }

}
