package groovy.lang;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.IteratorClosureAdapter;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class ObjectRange extends AbstractList implements Range {
   private Comparable from;
   private Comparable to;
   private int size = -1;
   private final boolean reverse;

   public ObjectRange(Comparable from, Comparable to) {
      if (from == null) {
         throw new IllegalArgumentException("Must specify a non-null value for the 'from' index in a Range");
      } else if (to == null) {
         throw new IllegalArgumentException("Must specify a non-null value for the 'to' index in a Range");
      } else {
         try {
            this.reverse = ScriptBytecodeAdapter.compareGreaterThan(from, to);
         } catch (ClassCastException var4) {
            throw new IllegalArgumentException("Unable to create range due to incompatible types: " + from.getClass().getSimpleName() + ".." + to.getClass().getSimpleName() + " (possible missing brackets around range?)", var4);
         }

         if (this.reverse) {
            this.constructorHelper(to, from);
         } else {
            this.constructorHelper(from, to);
         }

      }
   }

   public ObjectRange(Comparable from, Comparable to, boolean reverse) {
      this.constructorHelper(from, to);
      this.reverse = reverse;
   }

   private void constructorHelper(Comparable from, Comparable to) {
      if (from instanceof Short) {
         from = ((Short)from).intValue();
      } else if (from instanceof Float) {
         from = ((Float)from).doubleValue();
      }

      if (to instanceof Short) {
         to = ((Short)to).intValue();
      } else if (to instanceof Float) {
         to = ((Float)to).doubleValue();
      }

      if (from.getClass() == to.getClass()) {
         this.from = (Comparable)from;
         this.to = (Comparable)to;
      } else {
         this.from = normaliseStringType((Comparable)from);
         this.to = normaliseStringType((Comparable)to);
      }

      if (from instanceof String || to instanceof String) {
         String start = from.toString();
         String end = to.toString();
         if (start.length() > end.length()) {
            throw new IllegalArgumentException("Incompatible Strings for Range: starting String is longer than ending string");
         }

         int length = Math.min(start.length(), end.length());

         int i;
         for(i = 0; i < length && start.charAt(i) == end.charAt(i); ++i) {
         }

         if (i < length - 1) {
            throw new IllegalArgumentException("Incompatible Strings for Range: String#next() will not reach the expected value");
         }
      }

   }

   public boolean equals(Object that) {
      return that instanceof ObjectRange ? this.equals((ObjectRange)that) : super.equals(that);
   }

   public boolean equals(ObjectRange that) {
      return that != null && this.reverse == that.reverse && DefaultTypeTransformation.compareEqual(this.from, that.from) && DefaultTypeTransformation.compareEqual(this.to, that.to);
   }

   public Comparable getFrom() {
      return this.from;
   }

   public Comparable getTo() {
      return this.to;
   }

   public boolean isReverse() {
      return this.reverse;
   }

   public Object get(int index) {
      if (index < 0) {
         throw new IndexOutOfBoundsException("Index: " + index + " should not be negative");
      } else if (index >= this.size()) {
         throw new IndexOutOfBoundsException("Index: " + index + " is too big for range: " + this);
      } else {
         Object value;
         int i;
         if (this.reverse) {
            value = this.to;

            for(i = 0; i < index; ++i) {
               value = this.decrement(value);
            }
         } else {
            value = this.from;

            for(i = 0; i < index; ++i) {
               value = this.increment(value);
            }
         }

         return value;
      }
   }

   public Iterator iterator() {
      return new Iterator() {
         private int index;
         private Object value;

         {
            this.value = ObjectRange.this.reverse ? ObjectRange.this.to : ObjectRange.this.from;
         }

         public boolean hasNext() {
            return this.index < ObjectRange.this.size();
         }

         public Object next() {
            if (this.index++ > 0) {
               if (this.index > ObjectRange.this.size()) {
                  this.value = null;
               } else if (ObjectRange.this.reverse) {
                  this.value = ObjectRange.this.decrement(this.value);
               } else {
                  this.value = ObjectRange.this.increment(this.value);
               }
            }

            return this.value;
         }

         public void remove() {
            ObjectRange.this.remove(this.index);
         }
      };
   }

   public boolean containsWithinBounds(Object value) {
      if (!(value instanceof Comparable)) {
         return this.contains(value);
      } else {
         int result = this.compareTo(this.from, (Comparable)value);
         return result == 0 || result < 0 && this.compareTo(this.to, (Comparable)value) >= 0;
      }
   }

   private int compareTo(Comparable first, Comparable second) {
      return DefaultGroovyMethods.numberAwareCompareTo(first, second);
   }

   public int size() {
      if (this.size == -1) {
         if ((this.from instanceof Integer || this.from instanceof Long) && (this.to instanceof Integer || this.to instanceof Long)) {
            long fromNum = ((Number)this.from).longValue();
            long toNum = ((Number)this.to).longValue();
            this.size = (int)(toNum - fromNum + 1L);
         } else if (this.from instanceof Character && this.to instanceof Character) {
            char fromNum = (Character)this.from;
            char toNum = (Character)this.to;
            this.size = toNum - fromNum + 1;
         } else if (!(this.from instanceof BigDecimal) && !(this.to instanceof BigDecimal)) {
            this.size = 0;
            Comparable first = this.from;
            Comparable value = this.from;

            while(this.compareTo(this.to, value) >= 0) {
               value = (Comparable)this.increment(value);
               ++this.size;
               if (this.compareTo(first, value) >= 0) {
                  break;
               }
            }
         } else {
            BigDecimal fromNum = new BigDecimal("" + this.from);
            BigDecimal toNum = new BigDecimal("" + this.to);
            BigInteger sizeNum = toNum.subtract(fromNum).add(new BigDecimal(1.0D)).toBigInteger();
            this.size = sizeNum.intValue();
         }
      }

      return this.size;
   }

   public List subList(int fromIndex, int toIndex) {
      if (fromIndex < 0) {
         throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
      } else if (toIndex > this.size()) {
         throw new IndexOutOfBoundsException("toIndex = " + toIndex);
      } else if (fromIndex > toIndex) {
         throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
      } else if (fromIndex == toIndex) {
         return new EmptyRange(this.from);
      } else {
         Comparable var10002 = (Comparable)this.get(fromIndex);
         --toIndex;
         return new ObjectRange(var10002, (Comparable)this.get(toIndex), this.reverse);
      }
   }

   public String toString() {
      return this.reverse ? "" + this.to + ".." + this.from : "" + this.from + ".." + this.to;
   }

   public String inspect() {
      String toText = InvokerHelper.inspect(this.to);
      String fromText = InvokerHelper.inspect(this.from);
      return this.reverse ? "" + toText + ".." + fromText : "" + fromText + ".." + toText;
   }

   public boolean contains(Object value) {
      Iterator it = this.iterator();
      if (value == null) {
         return false;
      } else {
         while(it.hasNext()) {
            try {
               if (DefaultTypeTransformation.compareEqual(value, it.next())) {
                  return true;
               }
            } catch (ClassCastException var4) {
               return false;
            }
         }

         return false;
      }
   }

   public void step(int step, Closure closure) {
      if (step == 0) {
         if (this.compareTo(this.from, this.to) != 0) {
            throw new GroovyRuntimeException("Infinite loop detected due to step size of 0");
         }
      } else {
         if (this.reverse) {
            step = -step;
         }

         Comparable first;
         Comparable value;
         int i;
         if (step > 0) {
            first = this.from;
            value = this.from;

            while(this.compareTo(value, this.to) <= 0) {
               closure.call((Object)value);

               for(i = 0; i < step; ++i) {
                  value = (Comparable)this.increment(value);
                  if (this.compareTo(value, first) <= 0) {
                     return;
                  }
               }
            }
         } else {
            step = -step;
            first = this.to;
            value = this.to;

            while(this.compareTo(value, this.from) >= 0) {
               closure.call((Object)value);

               for(i = 0; i < step; ++i) {
                  value = (Comparable)this.decrement(value);
                  if (this.compareTo(value, first) >= 0) {
                     return;
                  }
               }
            }
         }

      }
   }

   public List step(int step) {
      IteratorClosureAdapter adapter = new IteratorClosureAdapter(this);
      this.step(step, adapter);
      return adapter.asList();
   }

   protected Object increment(Object value) {
      return InvokerHelper.invokeMethod(value, "next", (Object)null);
   }

   protected Object decrement(Object value) {
      return InvokerHelper.invokeMethod(value, "previous", (Object)null);
   }

   private static Comparable normaliseStringType(Comparable operand) {
      if (operand instanceof Character) {
         return Integer.valueOf((Character)operand);
      } else if (operand instanceof String) {
         String string = (String)operand;
         return (Comparable)(string.length() == 1 ? Integer.valueOf(string.charAt(0)) : string);
      } else {
         return operand;
      }
   }
}
