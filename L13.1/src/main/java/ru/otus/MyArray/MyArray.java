package ru.otus.MyArray;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class MyArray<T extends Comparable> {
    final private T[] arr;
    final private Class<T> tClass;
    final private int size;
    private MySubarray mySubarray1;
    private MySubarray mySubarray2;
    private MySubarray mySubarray3;
    private MySubarray mySubarray4;

    public MyArray(Class<T> tClass, int size, Supplier<T> fill) {
        this.size = size;
        this.tClass = tClass;
        arr = (T[]) Array.newInstance(tClass, size);
        for (int i = 0; i < size; i++) {
            this.arr[i] = fill.get();
        }
    }

    private void splitArray() {
        mySubarray1 = new MySubarray<T>("subarray 1", arr, 0, size / 4);
        mySubarray2 = new MySubarray<T>("subarray 2", arr, size / 4, size / 2);
        mySubarray3 = new MySubarray<T>("subarray 3", arr, size / 2, size / 4 * 3);
        mySubarray4 = new MySubarray<T>("subarray 4", arr, size / 4 * 3, size);
    }

    private void mergeArray() {
        merge(0, size / 4, size / 2);
        merge(size / 2, size / 4 * 3, size);
        merge(0, size / 2, size);
    }

    private void merge(int left, int mid, int right) {
        int it1 = 0;
        int it2 = 0;
        T result[] = (T[]) Array.newInstance(tClass, right - left);
        while (left + it1 < mid && mid + it2 < right) {
            if (arr[left + it1].compareTo(arr[mid + it2]) < 0) {
                result[it1 + it2] = arr[left + it1];
                it1 += 1;
            } else {
                result[it1 + it2] = arr[mid + it2];
                it2 += 1;
            }
        }
        while (left + it1 < mid) {
            result[it1 + it2] = arr[left + it1];
            it1 += 1;
        }
        while (mid + it2 < right) {
            result[it1 + it2] = arr[mid + it2];
            it2 += 1;
        }
        System.arraycopy(result, 0, arr, left, it1 + it2);
    }

    public T[] getArr() {
        return arr;
    }

    public int getSize() {
        return size;
    }

    public void sort() throws InterruptedException {
        SortingInThreads sortingInThreads = new SortingInThreads();
        splitArray();
        sortingInThreads.sort(mySubarray1, mySubarray2, mySubarray3, mySubarray4);
        mergeArray();
    }

    @Override
    public String toString() {
        return "MyArray=" + Arrays.toString(arr);
    }
}
