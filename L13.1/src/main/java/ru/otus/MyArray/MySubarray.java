package ru.otus.MyArray;

import java.util.Arrays;

@SuppressWarnings("unchecked")
public class MySubarray<T extends Comparable> implements Runnable{
    private String name;
    private T arr[];
    private int left;
    private int right;

    public MySubarray(String name, T[] arr, int left, int right) {
        this.name = name;
        this.arr = arr;
        this.left = left >= 0 ? left : 0;
        this.right = left <= right ? right : left;
    }

    @Override
    public void run() {
        for (int i = right - 1; i >= left; i--) {
            for (int j = left; j < i; j++) {
                if(arr[j].compareTo(arr[j + 1]) > 0)
                    swap(j, j + 1);
            }
        }
    }

    private void swap(int first, int second) {
        T a = arr[first];
        arr[first] = arr[second];
        arr[second] = a;
    }

    @Override
    public String toString() {
        return name + "=" + Arrays.toString(Arrays.copyOfRange(arr, left, right));
    }
}
