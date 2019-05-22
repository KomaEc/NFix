package groovy.lang;

import java.math.BigInteger;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.runtime.IteratorClosureAdapter;

public class IntRange extends AbstractList<Integer> implements Range<Integer> {
   private int from;
   private int to;
   private boolean reverse;

   public IntRange(int from, int to) {
      if (from > to) {
         this.from = to;
         this.to = from;
         this.reverse = true;
      } else {
         this.from = from;
         this.to = to;
      }

      if (this.to - this.from >= Integer.MAX_VALUE) {
         throw new IllegalArgumentException("range must have no more than 2147483647 elements");
      }
   }

   protected IntRange(int from, int to, boolean reverse) {
      if (from > to) {
         throw new IllegalArgumentException("'from' must be less than or equal to 'to'");
      } else {
         this.from = from;
         this.to = to;
         this.reverse = reverse;
      }
   }

   public boolean equals(Object that) {
      return that instanceof IntRange ? this.equals((IntRange)that) : super.equals(that);
   }

   public boolean equals(IntRange that) {
      return that != null && this.reverse == that.reverse && this.from == that.from && this.to == that.to;
   }

   public Comparable getFrom() {
      return this.from;
   }

   public Comparable getTo() {
      return this.to;
   }

   public int getFromInt() {
      return this.from;
   }

   public int getToInt() {
      return this.to;
   }

   public boolean isReverse() {
      return this.reverse;
   }

   public boolean containsWithinBounds(Object o) {
      return this.contains(o);
   }

   public Integer get(int index) {
      if (index < 0) {
         throw new IndexOutOfBoundsException("Index: " + index + " should not be negative");
      } else if (index >= this.size()) {
         throw new IndexOutOfBoundsException("Index: " + index + " too big for range: " + this);
      } else {
         int value = this.reverse ? this.to - index : index + this.from;
         return value;
      }
   }

   public int size() {
      return this.to - this.from + 1;
   }

   public Iterator<Integer> iterator() {
      return new IntRange.IntRangeIterator();
   }

   public List<Integer> subList(int fromIndex, int toIndex) {
      if (fromIndex < 0) {
         throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
      } else if (toIndex > this.size()) {
         throw new IndexOutOfBoundsException("toIndex = " + toIndex);
      } else if (fromIndex > toIndex) {
         throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
      } else {
         return (List)(fromIndex == toIndex ? new EmptyRange(this.from) : new IntRange(fromIndex + this.from, toIndex + this.from - 1, this.reverse));
      }
   }

   public String toString() {
      return this.reverse ? "" + this.to + ".." + this.from : "" + this.from + ".." + this.to;
   }

   public String inspect() {
      return this.toString();
   }

   public boolean contains(Object value) {
      if (value instanceof Integer) {
         Integer integer = (Integer)value;
         int i = integer;
         return i >= this.from && i <= this.to;
      } else if (!(value instanceof BigInteger)) {
         return false;
      } else {
         BigInteger bigint = (BigInteger)value;
         return bigint.compareTo(BigInteger.valueOf((long)this.from)) >= 0 && bigint.compareTo(BigInteger.valueOf((long)this.to)) <= 0;
      }
   }

   public boolean containsAll(Collection other) {
      if (!(other instanceof IntRange)) {
         return super.containsAll(other);
      } else {
         IntRange range = (IntRange)other;
         return this.from <= range.from && range.to <= this.to;
      }
   }

   public void step(int step, Closure closure) {
      if (step == 0) {
         if (this.from != this.to) {
            throw new GroovyRuntimeException("Infinite loop detected due to step size of 0");
         }
      } else {
         if (this.reverse) {
            step = -step;
         }

         int value;
         if (step > 0) {
            for(value = this.from; value <= this.to; value += step) {
               closure.call((Object)value);
            }
         } else {
            for(value = this.to; value >= this.from; value += step) {
               closure.call((Object)value);
            }
         }

      }
   }

   public List<Integer> step(int step) {
      IteratorClosureAdapter<Integer> adapter = new IteratorClosureAdapter(this);
      this.step(step, adapter);
      return adapter.asList();
   }

   private class IntRangeIterator implements Iterator<Integer> {
      private int index;
      private int size;
      private int value;

      private IntRangeIterator() {
         this.size = IntRange.this.size();
         this.value = IntRange.this.reverse ? IntRange.this.to : IntRange.this.from;
      }

      public boolean hasNext() {
         return this.index < this.size;
      }

      public Integer next() {
         if (this.index++ > 0) {
            if (this.index > this.size) {
               return null;
            }

            if (IntRange.this.reverse) {
               --this.value;
            } else {
               ++this.value;
            }
         }

         return this.value;
      }

      public void remove() {
         IntRange.this.remove(this.index);
      }

      // $FF: synthetic method
      IntRangeIterator(Object x1) {
         this();
      }
   }
}
