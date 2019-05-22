package org.apache.maven.monitor.event;

public interface EventDispatcher {
   void addEventMonitor(EventMonitor var1);

   void dispatchStart(String var1, String var2);

   void dispatchEnd(String var1, String var2);

   void dispatchError(String var1, String var2, Throwable var3);
}
