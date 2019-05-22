package com.gzoltar.shaded.org.pitest.util;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.Iterator;
import java.util.List;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;

public class MemoryWatchdog {
   public static void addWatchDogToAllPools(long threshold, NotificationListener listener) {
      MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
      NotificationEmitter ne = (NotificationEmitter)memBean;
      ne.addNotificationListener(listener, (NotificationFilter)null, (Object)null);
      List<MemoryPoolMXBean> memPools = ManagementFactory.getMemoryPoolMXBeans();
      Iterator i$ = memPools.iterator();

      while(i$.hasNext()) {
         MemoryPoolMXBean mp = (MemoryPoolMXBean)i$.next();
         if (mp.isUsageThresholdSupported()) {
            MemoryUsage mu = mp.getUsage();
            long max = mu.getMax();
            long alert = max * threshold / 100L;
            mp.setUsageThreshold(alert);
         }
      }

   }
}
