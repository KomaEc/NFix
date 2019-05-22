package org.apache.maven.monitor.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultEventDispatcher implements EventDispatcher {
   private List eventMonitors = new ArrayList();

   public void addEventMonitor(EventMonitor monitor) {
      this.eventMonitors.add(monitor);
   }

   public void dispatchStart(String event, String target) {
      Iterator it = this.eventMonitors.iterator();

      while(it.hasNext()) {
         EventMonitor monitor = (EventMonitor)it.next();
         monitor.startEvent(event, target, System.currentTimeMillis());
      }

   }

   public void dispatchEnd(String event, String target) {
      Iterator it = this.eventMonitors.iterator();

      while(it.hasNext()) {
         EventMonitor monitor = (EventMonitor)it.next();
         monitor.endEvent(event, target, System.currentTimeMillis());
      }

   }

   public void dispatchError(String event, String target, Throwable cause) {
      Iterator it = this.eventMonitors.iterator();

      while(it.hasNext()) {
         EventMonitor monitor = (EventMonitor)it.next();
         monitor.errorEvent(event, target, System.currentTimeMillis(), cause);
      }

   }
}
