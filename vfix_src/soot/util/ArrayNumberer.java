package soot.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayNumberer<E extends Numberable> implements IterableNumberer<E> {
   protected E[] numberToObj;
   protected int lastNumber;

   public ArrayNumberer() {
      this.numberToObj = (Numberable[])(new Numberable[1024]);
      this.lastNumber = 0;
   }

   public ArrayNumberer(E[] elements) {
      this.numberToObj = elements;
      this.lastNumber = elements.length;
   }

   private void resize(int n) {
      this.numberToObj = (Numberable[])Arrays.copyOf(this.numberToObj, n);
   }

   public synchronized void add(E o) {
      if (o.getNumber() == 0) {
         ++this.lastNumber;
         if (this.lastNumber >= this.numberToObj.length) {
            this.resize(this.numberToObj.length * 2);
         }

         this.numberToObj[this.lastNumber] = o;
         o.setNumber(this.lastNumber);
      }
   }

   public long get(E o) {
      if (o == null) {
         return 0L;
      } else {
         int ret = o.getNumber();
         if (ret == 0) {
            throw new RuntimeException("unnumbered: " + o);
         } else {
            return (long)ret;
         }
      }
   }

   public E get(long number) {
      if (number == 0L) {
         return null;
      } else {
         E ret = this.numberToObj[(int)number];
         if (ret == null) {
            throw new RuntimeException("no object with number " + number);
         } else {
            return ret;
         }
      }
   }

   public int size() {
      return this.lastNumber;
   }

   public Iterator<E> iterator() {
      return new Iterator<E>() {
         int cur = 1;

         public final boolean hasNext() {
            return this.cur <= ArrayNumberer.this.lastNumber && this.cur < ArrayNumberer.this.numberToObj.length && ArrayNumberer.this.numberToObj[this.cur] != null;
         }

         public final E next() {
            if (this.hasNext()) {
               return ArrayNumberer.this.numberToObj[this.cur++];
            } else {
               throw new NoSuchElementException();
            }
         }

         public final void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }
}
