package org.testng.internal;

import java.util.Iterator;

class ArrayIterator implements Iterator<Object[]> {
   private Object[][] m_objects;
   private int m_count;

   public ArrayIterator(Object[][] objects) {
      this.m_objects = objects;
      this.m_count = 0;
   }

   public boolean hasNext() {
      return this.m_count < this.m_objects.length;
   }

   public Object[] next() {
      return this.m_objects[this.m_count++];
   }

   public void remove() {
      throw new UnsupportedOperationException("Remove operation is not supported on this iterator");
   }
}
