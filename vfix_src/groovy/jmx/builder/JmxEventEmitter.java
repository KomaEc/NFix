package groovy.jmx.builder;

import java.util.concurrent.atomic.AtomicLong;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class JmxEventEmitter extends NotificationBroadcasterSupport implements JmxEventEmitterMBean {
   private String event;
   private String message;

   public String getEvent() {
      return this.event;
   }

   public void setEvent(String event) {
      this.event = event;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public long send(Object data) {
      long seq = JmxEventEmitter.NumberSequencer.getNextSequence();
      Notification note = new Notification(this.getEvent(), this, seq, System.currentTimeMillis(), "Event notification " + this.getEvent());
      note.setUserData(data);
      super.sendNotification(note);
      return seq;
   }

   private static class NumberSequencer {
      private static AtomicLong num = new AtomicLong(0L);

      public static long getNextSequence() {
         return num.incrementAndGet();
      }
   }
}
