package org.apache.maven.monitor.event;

public interface EventMonitor {
   void startEvent(String var1, String var2, long var3);

   void endEvent(String var1, String var2, long var3);

   void errorEvent(String var1, String var2, long var3, Throwable var5);
}
