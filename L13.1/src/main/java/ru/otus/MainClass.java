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
        Integer arrayCopy[] = new Integer[myArray.getSize()];
        System.arraycopy(myArray.getArr(), 0, arrayCopy, 0, myArray.getSize());
        MySubarray mySubarray = new MySubarray("MyArray copy", arrayCopy, 0, arrayCopy.length);
        System.out.println("Before sorting");
        System.out.println(myArray);

        long startTime = System.currentTimeMillis();
        myArray.sort();
        long endTime = System.currentTimeMillis();
        System.out.println("After sorting");
        System.out.println(myArray);
        System.out.println("Sorting time (4 threads) = " + (endTime - startTime) + " ms");

        SortingInThreads sortingInThreads = new SortingInThreads();
        sortingInThreads.sort(mySubarray);
        System.out.println(mySubarray);
        System.out.println("Sorting time (1 thread) = " +
                (sortingInThreads.getEndTime() - sortingInThreads.getStartTime()) + " ms");
    }

}
