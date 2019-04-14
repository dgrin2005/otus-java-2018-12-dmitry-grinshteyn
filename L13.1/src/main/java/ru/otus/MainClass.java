package ru.otus;

import ru.otus.MyArray.MyArray;
import ru.otus.MyArray.MySubarray;

public class MainClass {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws InterruptedException {

        MyArray myArray = new MyArray(Integer.class,
                10000,
                () -> Math.toIntExact(Math.round(Math.random() * 10000)));
        myArray.splitArray();
        MySubarray mySubarray1 = myArray.getMySubarray1();
        MySubarray mySubarray2 = myArray.getMySubarray2();
        MySubarray mySubarray3 = myArray.getMySubarray3();
        MySubarray mySubarray4 = myArray.getMySubarray4();
        System.out.println("Before sorting");
        System.out.println(myArray);
        MySubarray mySubarray = new MySubarray("MyArray copy", myArray.getArr(), 0, myArray.getSize());
        long startTime = System.currentTimeMillis();
        Thread t1 = new Thread(mySubarray1);
        Thread t2 = new Thread(mySubarray2);
        Thread t3 = new Thread(mySubarray3);
        Thread t4 = new Thread(mySubarray4);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        long endTime = System.currentTimeMillis();
        myArray.mergeArray();
        System.out.println("After sorting");
        System.out.println(myArray);
        System.out.println("Sorting time (4 threads) = " + (endTime - startTime) + " ms");
        startTime = System.currentTimeMillis();
        Thread t = new Thread(mySubarray);
        t.start();
        t.join();
        endTime = System.currentTimeMillis();
        System.out.println(mySubarray);
        System.out.println("Sorting time (1 thread) = " + (endTime - startTime) + " ms");
    }


}
