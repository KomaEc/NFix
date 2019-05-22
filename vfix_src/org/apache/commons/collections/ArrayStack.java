package org.apache.commons.collections;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class ArrayStack extends ArrayList implements Buffer {
   private static final long serialVersionUID = 2130079159931574599L;

   public ArrayStack() {
   }

   public ArrayStack(int initialSize) {
      super(initialSize);
   }

   public boolean empty() {
      return this.isEmpty();
   }

   public Object peek() throws EmptyStackException {
      int n = this.size();
      if (n <= 0) {
         throw new EmptyStackException();
      } else {
         return this.get(n - 1);
      }
   }

   public Object peek(int n) throws EmptyStackException {
      int m = this.size() - n - 1;
      if (m < 0) {
         throw new EmptyStackException();
      } else {
         return this.get(m);
      }
   }

   public Object pop() throws EmptyStackException {
      int n = this.size();
      if (n <= 0) {
         throw new EmptyStackException();
      } else {
         return this.remove(n - 1);
      }
   }

   public Object push(Object item) {
      this.add(item);
      return item;
   }

   public int search(Object object) {
      int i = this.size() - 1;

      for(int n = 1; i >= 0; ++n) {
         Object current = this.get(i);
         if (object == null && current == null || object != null && object.equals(current)) {
            return n;
         }

         --i;
      }

      return -1;
   }

   public Object get() {
      int size = this.size();
      if (size == 0) {
         throw new BufferUnderflowException();
      } else {
         return this.get(size - 1);
      }
   }

   public Object remove() {
      int size = this.size();
      if (size == 0) {
         throw new BufferUnderflowException();
      } else {
         return this.remove(size - 1);
      }
   }
}
