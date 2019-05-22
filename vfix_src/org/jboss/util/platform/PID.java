package org.jboss.util.platform;

import java.io.Serializable;
import java.util.Random;

public class PID implements Serializable, Cloneable {
   private static final long serialVersionUID = -6837013326314943907L;
   protected final int id;
   private static PID instance = null;

   protected PID(int id) {
      this.id = id;
   }

   public final int getID() {
      return this.id;
   }

   public String toString() {
      return String.valueOf(this.id);
   }

   public String toString(int radix) {
      return Integer.toString(this.id, radix);
   }

   public int hashCode() {
      return this.id;
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (obj != null && obj.getClass() == this.getClass()) {
         PID pid = (PID)obj;
         return pid.id == this.id;
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

   public static synchronized PID getInstance() {
      if (instance == null) {
         instance = create();
      }

      return instance;
   }

   private static PID create() {
      int random = Math.abs((new Random()).nextInt());
      return new PID(random);
   }
}
