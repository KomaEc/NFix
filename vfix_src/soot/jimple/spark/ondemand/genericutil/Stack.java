package soot.jimple.spark.ondemand.genericutil;

import java.util.Collection;
import java.util.Iterator;

public final class Stack<T> implements Cloneable {
   private T[] elems;
   private int size;

   public Stack(int numElems_) {
      this.size = 0;
      this.elems = (Object[])(new Object[numElems_]);
   }

   public Stack() {
      this(4);
   }

   public void push(T obj_) {
      assert obj_ != null;

      if (this.size == this.elems.length) {
         Object[] tmp = this.elems;
         this.elems = (Object[])(new Object[tmp.length * 2]);
         System.arraycopy(tmp, 0, this.elems, 0, tmp.length);
      }

      this.elems[this.size] = obj_;
      ++this.size;
   }

   public void pushAll(Collection<T> c) {
      Iterator var2 = c.iterator();

      while(var2.hasNext()) {
         T t = var2.next();
         this.push(t);
      }

   }

   public T pop() {
      if (this.size == 0) {
         return null;
      } else {
         --this.size;
         T ret = this.elems[this.size];
         this.elems[this.size] = null;
         return ret;
      }
   }

   public T peek() {
      return this.size == 0 ? null : this.elems[this.size - 1];
   }

   public int size() {
      return this.size;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public void clear() {
      this.size = 0;
   }

   public Stack<T> clone() {
      Stack ret = null;

      try {
         ret = (Stack)super.clone();
         ret.elems = (Object[])(new Object[this.elems.length]);
         System.arraycopy(this.elems, 0, ret.elems, 0, this.size);
         return ret;
      } catch (CloneNotSupportedException var3) {
         throw new InternalError();
      }
   }

   public Object get(int i) {
      return this.elems[i];
   }

   public boolean contains(Object o) {
      return Util.arrayContains(this.elems, o, this.size);
   }

   public int indexOf(T o) {
      for(int i = 0; i < this.size && this.elems[i] != null; ++i) {
         if (this.elems[i].equals(o)) {
            return i;
         }
      }

      return -1;
   }

   public String toString() {
      StringBuffer s = new StringBuffer();
      s.append("[");

      for(int i = 0; i < this.size && this.elems[i] != null; ++i) {
         if (i > 0) {
            s.append(", ");
         }

         s.append(this.elems[i].toString());
      }

      s.append("]");
      return s.toString();
   }
}
