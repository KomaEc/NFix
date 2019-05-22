package org.jboss.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

public final class SoftObject extends SoftReference {
   protected final int hashCode;

   public SoftObject(Object obj) {
      super(obj);
      this.hashCode = obj.hashCode();
   }

   public SoftObject(Object obj, ReferenceQueue queue) {
      super(obj, queue);
      this.hashCode = obj.hashCode();
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (obj != null && obj.getClass() == this.getClass()) {
         SoftObject soft = (SoftObject)obj;
         Object a = this.get();
         Object b = soft.get();
         if (a != null && b != null) {
            return a == b ? true : a.equals(b);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.hashCode;
   }

   public static SoftObject create(Object obj) {
      return obj == null ? null : new SoftObject(obj);
   }

   public static SoftObject create(Object obj, ReferenceQueue queue) {
      return obj == null ? null : new SoftObject(obj, queue);
   }
}
