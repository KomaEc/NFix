package org.apache.maven.wagon.events;

import java.util.EventObject;
import org.apache.maven.wagon.Wagon;

public class WagonEvent extends EventObject {
   protected long timestamp;

   public WagonEvent(Wagon source) {
      super(source);
   }

   public Wagon getWagon() {
      return (Wagon)this.getSource();
   }

   public long getTimestamp() {
      return this.timestamp;
   }

   public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
   }
}
