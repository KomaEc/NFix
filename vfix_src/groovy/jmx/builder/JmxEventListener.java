package groovy.jmx.builder;

import groovy.lang.Closure;
import java.util.HashMap;
import java.util.Map;
import javax.management.Notification;
import javax.management.NotificationListener;

public class JmxEventListener implements NotificationListener {
   private static JmxEventListener listener;

   public static synchronized JmxEventListener getListner() {
      if (listener == null) {
         listener = new JmxEventListener();
      }

      return listener;
   }

   public void handleNotification(Notification notification, Object handback) {
      Map event = (Map)handback;
      if (event != null) {
         Object del = event.get("managedObject");
         Object callback = event.get("callback");
         if (callback != null && callback instanceof Closure) {
            Closure closure = (Closure)callback;
            closure.setDelegate(del);
            if (closure.getMaximumNumberOfParameters() == 1) {
               closure.call((Object)buildOperationNotificationPacket(notification));
            } else {
               closure.call();
            }
         }
      }

   }

   private static Map buildOperationNotificationPacket(Notification note) {
      Map<String, Object> result = new HashMap();
      result.put("event", note.getType());
      result.put("source", note.getSource());
      result.put("sequenceNumber", note.getSequenceNumber());
      result.put("timeStamp", note.getTimeStamp());
      result.put("data", note.getUserData());
      return result;
   }
}
