package ru.otus;

/*
VM Options:
-Xms18m -Xmx18m -XX:MaxMetaspaceSize=256m
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MainClass {

    public static void main(String[] args) {
        Map<String, List<Integer>> gcQuantityMap = new HashMap<>();
        Map<String, List<Double>> gcDurationMap = new HashMap<>();
        TestMemory testMemory = new TestMemory();
        long begin = System.currentTimeMillis();
        GCListener gcListener = new GCListener(begin, gcQuantityMap, gcDurationMap);
        try {
            testMemory.run();
        } catch (OutOfMemoryError e) {
            System.out.println(e);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            int minutes = 0;
            for (final Map.Entry<String, List<Integer>> entry : gcQuantityMap.entrySet()) {
                minutes = Math.max(minutes, entry.getValue().size());
            }
            for (final Map.Entry<String, List<Integer>> entry : gcQuantityMap.entrySet()) {
                List<Integer> quantityList = entry.getValue();
                Integer quantityListSize = quantityList.size();
                for (int i = quantityListSize; i < minutes; i++) {
                    quantityList.add(i, 0);
                }
            }
            for (final Map.Entry<String, List<Double>> entry : gcDurationMap.entrySet()) {
                List<Double> durationList = entry.getValue();
                Integer durationListSize = durationList.size();
                for (int i = durationListSize; i < minutes; i++) {
                    durationList.add(i, 0.);
                }
            }
            StringBuilder str = new StringBuilder("");
            str.append("Minutes:" + "\t");
            for (int i = 1; i <= minutes; i++) {
                str.append(i).append("\t");
            }
            log.info(String.valueOf(str));
            log.info("Quantity:");
            for (final Map.Entry<String, List<Integer>> entry : gcQuantityMap.entrySet()) {
                str = new StringBuilder("");
                str.append(entry.getKey()).append(":").append("\t");
                for (Integer value : entry.getValue()) {
                    str.append(value).append("\t");
                }
                log.info(String.valueOf(str));
            }
            log.info("Duration (ms):");
            for (final Map.Entry<String, List<Double>> entry : gcDurationMap.entrySet()) {
                str = new StringBuilder("");
                str.append(entry.getKey()).append(":").append("\t");
                for (Double value : entry.getValue()) {
                    str.append(value).append("\t");
                }
                log.info(String.valueOf(str));
            }
        }
    }

    private static Logger log = null;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        log = Logger.getLogger(MainClass.class.getName());
    }

}
