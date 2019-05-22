package org.jboss.util.threadpool;

import org.jboss.util.loading.ClassLoaderSource;

public interface BasicThreadPoolMBean extends ThreadPoolMBean {
   int getQueueSize();

   int getMaximumQueueSize();

   void setMaximumQueueSize(int var1);

   BlockingMode getBlockingMode();

   void setBlockingMode(BlockingMode var1);

   String getThreadGroupName();

   void setThreadGroupName(String var1);

   long getKeepAliveTime();

   void setKeepAliveTime(long var1);

   ClassLoaderSource getClassLoaderSource();

   void setClassLoaderSource(ClassLoaderSource var1);
}
