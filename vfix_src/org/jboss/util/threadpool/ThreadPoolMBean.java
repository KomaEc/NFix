package org.jboss.util.threadpool;

public interface ThreadPoolMBean {
   String getName();

   void setName(String var1);

   int getPoolNumber();

   int getMinimumPoolSize();

   void setMinimumPoolSize(int var1);

   int getMaximumPoolSize();

   void setMaximumPoolSize(int var1);

   int getPoolSize();

   ThreadPool getInstance();

   void stop();
}
