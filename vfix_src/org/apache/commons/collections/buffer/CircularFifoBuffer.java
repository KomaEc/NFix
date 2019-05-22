package org.apache.commons.collections.buffer;

import java.util.Collection;

public class CircularFifoBuffer extends BoundedFifoBuffer {
   private static final long serialVersionUID = -8423413834657610406L;

   public CircularFifoBuffer() {
      super(32);
   }

   public CircularFifoBuffer(int size) {
      super(size);
   }

   public CircularFifoBuffer(Collection coll) {
      super(coll);
   }

   public boolean add(Object element) {
      if (this.isFull()) {
         this.remove();
      }

      return super.add(element);
   }
}
