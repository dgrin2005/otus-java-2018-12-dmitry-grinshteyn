package ru.otus.MyArray;

import java.util.ArrayList;
import java.util.List;

public class SortingInThreads {
    private long startTime;
    private long endTime;

    public SortingInThreads() {
    }

    public void sort(MySubarray ... mySubarray) throws InterruptedException {
        List<Thread> threadList = new ArrayList<>();
        startTime = System.currentTimeMillis();
        for (MySubarray aMySubarray : mySubarray) {
            threadList.add(new Thread(aMySubarray));
        }
        for (Thread aThreadList : threadList) {
            aThreadList.start();
        }
        for (Thread aThreadList : threadList) {
            aThreadList.join();
        }
        endTime = System.currentTimeMillis();
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }
}
