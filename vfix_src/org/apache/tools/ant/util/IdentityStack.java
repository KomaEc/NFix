package org.apache.tools.ant.util;

import java.util.Stack;

public class IdentityStack extends Stack {
   public static IdentityStack getInstance(Stack s) {
      if (s instanceof IdentityStack) {
         return (IdentityStack)s;
      } else {
         IdentityStack result = new IdentityStack();
         if (s != null) {
            result.addAll(s);
         }

         return result;
      }
   }

   public IdentityStack() {
   }

   public IdentityStack(Object o) {
      this.push(o);
   }

   public synchronized boolean contains(Object o) {
      return this.indexOf(o) >= 0;
   }

   public synchronized int indexOf(Object o, int pos) {
      for(int i = pos; i < this.size(); ++i) {
         if (this.get(i) == o) {
            return i;
         }
      }

      return -1;
   }

   public synchronized int lastIndexOf(Object o, int pos) {
      for(int i = pos; i >= 0; --i) {
         if (this.get(i) == o) {
            return i;
         }
      }

      return -1;
   }
}
