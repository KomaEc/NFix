package org.apache.velocity.runtime.log;

import java.util.Iterator;
import java.util.Vector;
import org.apache.velocity.runtime.RuntimeServices;

class HoldingLogChute implements LogChute {
   private Vector pendingMessages = new Vector();

   public void init(RuntimeServices rs) throws Exception {
   }

   public void log(int level, String message) {
      synchronized(this) {
         Object[] data = new Object[]{new Integer(level), message};
         this.pendingMessages.addElement(data);
      }
   }

   public void log(int level, String message, Throwable t) {
      synchronized(this) {
         Object[] data = new Object[]{new Integer(level), message, t};
         this.pendingMessages.addElement(data);
      }
   }

   public boolean isLevelEnabled(int level) {
      return true;
   }

   public void transferTo(LogChute newChute) {
      synchronized(this) {
         if (!this.pendingMessages.isEmpty()) {
            Iterator i = this.pendingMessages.iterator();

            while(i.hasNext()) {
               Object[] data = (Object[])i.next();
               int level = (Integer)data[0];
               String message = (String)data[1];
               if (data.length == 2) {
                  newChute.log(level, message);
               } else {
                  newChute.log(level, message, (Throwable)data[2]);
               }
            }
         }

      }
   }
}
