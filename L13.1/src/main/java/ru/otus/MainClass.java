package ru.otus;

import ru.otus.MyArray.MyArray;
import ru.otus.MyArray.MySubarray;
import ru.otus.MyArray.SortingInThreads;

public class MainClass {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws InterruptedException {

        MyArray myArray = new MyArray(Integer.class,
                10000,
                () -> Math.toIntExact(Math.round(Math.random() * 10000)));
        MySubarray mySubarray = new MySubarray("MyArray copy", myArray.getArr(), 0, myArray.getSize());
        System.out.println("Before sorting");
        System.out.println(myArray);
        SortingInThreads sortingInThreads = new SortingInThreads();

        long startTime = System.currentTimeMillis();
        myArray.splitArray();
        MySubarray mySubarray1 = myArray.getMySubarray1();
        MySubarray mySubarray2 = myArray.getMySubarray2();
        MySubarray mySubarray3 = myArray.getMySubarray3();
        MySubarray mySubarray4 = myArray.getMySubarray4();
        sortingInThreads.sort(mySubarray1, mySubarray2, mySubarray3, mySubarray4);
        myArray.mergeArray();
        long endTime = System.currentTimeMillis();
        System.out.println("After sorting");
        System.out.println(myArray);
        System.out.println("Sorting time (4 threads) = " + (endTime - startTime) + " ms");

        sortingInThreads.sort(mySubarray);
        System.out.println(mySubarray);
        System.out.println("Sorting time (1 thread) = " +
                (sortingInThreads.getEndTime() - sortingInThreads.getStartTime()) + " ms");
    }

}
