package org.apache.commons.validator.util;

import java.io.Serializable;

public class Flags implements Serializable {
   private long flags = 0L;

   public Flags() {
   }

   public Flags(long flags) {
      this.flags = flags;
   }

   public long getFlags() {
      return this.flags;
   }

   public boolean isOn(long flag) {
      return (this.flags & flag) > 0L;
   }

   public boolean isOff(long flag) {
      return (this.flags & flag) == 0L;
   }

   public void turnOn(long flag) {
      this.flags |= flag;
   }

   public void turnOff(long flag) {
      this.flags &= ~flag;
   }

   public void turnOffAll() {
      this.flags = 0L;
   }

   public void clear() {
      this.flags = 0L;
   }

   public void turnOnAll() {
      this.flags = Long.MAX_VALUE;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException("Couldn't clone Flags object.");
      }
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof Flags)) {
         return false;
      } else if (obj == this) {
         return true;
      } else {
         Flags f = (Flags)obj;
         return this.flags == f.flags;
      }
   }

   public int hashCode() {
      return (int)this.flags;
   }

   public String toString() {
      StringBuffer bin = new StringBuffer(Long.toBinaryString(this.flags));

      for(int i = 64 - bin.length(); i > 0; --i) {
         bin.insert(0, "0");
      }

      return bin.toString();
   }
}
