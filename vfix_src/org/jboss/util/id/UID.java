package org.jboss.util.id;

import java.util.concurrent.atomic.AtomicLong;

public class UID implements ID {
   private static final long serialVersionUID = -8093336932569424512L;
   protected static final AtomicLong COUNTER = new AtomicLong(0L);
   protected final long time;
   protected final long id;

   public UID() {
      this.time = System.currentTimeMillis();
      this.id = COUNTER.incrementAndGet();
   }

   protected UID(UID uid) {
      this.time = uid.time;
      this.id = uid.id;
   }

   public final long getTime() {
      return this.time;
   }

   public final long getID() {
      return this.id;
   }

   public String toString() {
      return Long.toString(this.time, 36) + "-" + Long.toString(this.id, 36);
   }

   public int hashCode() {
      return (int)this.id;
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (obj != null && obj.getClass() == this.getClass()) {
         UID uid = (UID)obj;
         return uid.time == this.time && uid.id == this.id;
      } else {
         return false;
      }
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }

   public static String asString() {
      return (new UID()).toString();
   }
}
