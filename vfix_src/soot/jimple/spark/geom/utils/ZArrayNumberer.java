package soot.jimple.spark.geom.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.util.IterableNumberer;
import soot.util.Numberable;

public class ZArrayNumberer<E extends Numberable> implements IterableNumberer<E>, Iterable<E> {
   Numberable[] numberToObj = null;
   Map<E, E> objContainer = null;
   int lastNumber = 0;
   int filledCells = 0;

   public ZArrayNumberer() {
      this.numberToObj = new Numberable[1023];
      this.objContainer = new HashMap(1023);
   }

   public ZArrayNumberer(int initSize) {
      this.numberToObj = new Numberable[initSize];
      this.objContainer = new HashMap(initSize);
   }

   public void add(E o) {
      if (o.getNumber() == -1 || this.numberToObj[o.getNumber()] != o) {
         this.numberToObj[this.lastNumber] = o;
         o.setNumber(this.lastNumber);
         this.objContainer.put(o, o);
         ++this.lastNumber;
         ++this.filledCells;
         if (this.lastNumber >= this.numberToObj.length) {
            Numberable[] newnto = new Numberable[this.numberToObj.length * 2];
            System.arraycopy(this.numberToObj, 0, newnto, 0, this.numberToObj.length);
            this.numberToObj = newnto;
         }

      }
   }

   public void clear() {
      for(int i = 0; i < this.lastNumber; ++i) {
         this.numberToObj[i] = null;
      }

      this.lastNumber = 0;
      this.filledCells = 0;
      this.objContainer.clear();
   }

   public long get(E o) {
      return o == null ? -1L : (long)o.getNumber();
   }

   public E get(long number) {
      E ret = this.numberToObj[(int)number];
      return ret;
   }

   public E searchFor(E o) {
      return (Numberable)this.objContainer.get(o);
   }

   public boolean remove(E o) {
      int id = o.getNumber();
      if (id < 0) {
         return false;
      } else if (this.numberToObj[id] != o) {
         return false;
      } else {
         this.numberToObj[id] = null;
         o.setNumber(-1);
         --this.filledCells;
         return true;
      }
   }

   public int size() {
      return this.filledCells;
   }

   public void reassign() {
      int i = 0;

      for(int j = this.lastNumber - 1; i < j; ++i) {
         if (this.numberToObj[i] == null) {
            while(j > i && this.numberToObj[j] == null) {
               --j;
            }

            if (i == j) {
               break;
            }

            this.numberToObj[i] = this.numberToObj[j];
            this.numberToObj[i].setNumber(i);
            this.numberToObj[j] = null;
         }
      }

      this.lastNumber = i;
   }

   public Iterator<E> iterator() {
      return new ZArrayNumberer.NumbererIterator();
   }

   final class NumbererIterator implements Iterator<E> {
      int cur = 0;
      E lastElement = null;

      public final boolean hasNext() {
         while(this.cur < ZArrayNumberer.this.lastNumber && ZArrayNumberer.this.numberToObj[this.cur] == null) {
            ++this.cur;
         }

         return this.cur < ZArrayNumberer.this.lastNumber;
      }

      public final E next() {
         this.lastElement = ZArrayNumberer.this.numberToObj[this.cur++];
         return this.lastElement;
      }

      public final void remove() {
         ZArrayNumberer.this.remove(this.lastElement);
      }
   }
}
