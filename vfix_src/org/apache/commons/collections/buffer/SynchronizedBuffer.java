package org.apache.commons.collections.buffer;

import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.collection.SynchronizedCollection;

public class SynchronizedBuffer extends SynchronizedCollection implements Buffer {
   private static final long serialVersionUID = -6859936183953626253L;

   public static Buffer decorate(Buffer buffer) {
      return new SynchronizedBuffer(buffer);
   }

   protected SynchronizedBuffer(Buffer buffer) {
      super(buffer);
   }

   protected SynchronizedBuffer(Buffer buffer, Object lock) {
      super(buffer, lock);
   }

   protected Buffer getBuffer() {
      return (Buffer)super.collection;
   }

   public Object get() {
      Object var1 = super.lock;
      synchronized(var1) {
         Object var2 = this.getBuffer().get();
         return var2;
      }
   }

   public Object remove() {
      Object var1 = super.lock;
      synchronized(var1) {
         Object var2 = this.getBuffer().remove();
         return var2;
      }
   }
}
