package ru.otus;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GCListener {

    public GCListener(Long beginTime, Map<String, List<Integer>> gcQuantityMap, Map<String, List<Double>> gcDurationMap) {
        NotificationListener notificationListener = (notification, handback) -> {
            if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION))
            {
                GarbageCollectionNotificationInfo gcInformation = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                String gcName = gcInformation.getGcName();
                Long duration = gcInformation.getGcInfo().getDuration();
                Integer minutes = getCurrentMinute(beginTime);
                if (!gcQuantityMap.containsKey(gcName)) {
                    gcQuantityMap.put(gcName, new ArrayList<>());
                }
                List<Integer> quantityList = gcQuantityMap.get(gcName);
                Integer quantityListSize = quantityList.size();
                for (int i = quantityListSize; i <= minutes; i++) {
                    quantityList.add(i, 0);
                }
                Integer oldQuantity = quantityList.get(minutes);
                quantityList.remove((int) minutes);
                quantityList.add(minutes, oldQuantity + 1);
                if (!gcDurationMap.containsKey(gcName)) {
                    gcDurationMap.put(gcName, new ArrayList<>());
                }
                List<Double> durationList = gcDurationMap.get(gcName);
                Integer durationListSize = durationList.size();
                for (int i = durationListSize; i <= minutes; i++) {
                    durationList.add(i, 0.);
                }
                Double oldDuration = durationList.get(minutes);
                durationList.remove((int) minutes);
                durationList.add(minutes, oldDuration + duration);
            }
        };

        List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for(GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans) {
            NotificationEmitter emitter = (NotificationEmitter) garbageCollectorMXBean;
            emitter.addNotificationListener(notificationListener,null,null);
        }
    }

    private int getCurrentMinute(Long beginTime) {
        return (int) ((System.currentTimeMillis() - beginTime) / 60000);
    }
}
