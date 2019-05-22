package org.jboss.util;

import java.io.Serializable;

public final class Null implements Serializable {
   private static final long serialVersionUID = -403173436435490144L;
   public static final Null VALUE = new Null();

   private Null() {
   }

   public String toString() {
      return null;
   }

   public int hashCode() {
      return 0;
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else {
         return obj == null || obj.getClass() == this.getClass();
      }
   }
}
