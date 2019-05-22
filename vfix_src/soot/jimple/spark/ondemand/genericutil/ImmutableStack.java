package soot.jimple.spark.ondemand.genericutil;

import java.util.Arrays;

public class ImmutableStack<T> {
   private static final ImmutableStack<Object> EMPTY = new ImmutableStack(new Object[0]);
   private static final int MAX_SIZE = Integer.MAX_VALUE;
   private final T[] entries;

   public static int getMaxSize() {
      return Integer.MAX_VALUE;
   }

   public static final <T> ImmutableStack<T> emptyStack() {
      return EMPTY;
   }

   private ImmutableStack(T[] entries) {
      this.entries = entries;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && o instanceof ImmutableStack) {
         ImmutableStack other = (ImmutableStack)o;
         return Arrays.equals(this.entries, other.entries);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Util.hashArray(this.entries);
   }

   public ImmutableStack<T> push(T entry) {
      assert entry != null;

      int size = this.entries.length + 1;
      T[] tmpEntries = null;
      if (size <= Integer.MAX_VALUE) {
         tmpEntries = (Object[])(new Object[size]);
         System.arraycopy(this.entries, 0, tmpEntries, 0, this.entries.length);
         tmpEntries[size - 1] = entry;
      } else {
         tmpEntries = (Object[])(new Object[Integer.MAX_VALUE]);
         System.arraycopy(this.entries, 1, tmpEntries, 0, this.entries.length - 1);
         tmpEntries[2147483646] = entry;
      }

      return new ImmutableStack(tmpEntries);
   }

   public T peek() {
      assert this.entries.length != 0;

      return this.entries[this.entries.length - 1];
   }

   public ImmutableStack<T> pop() {
      assert this.entries.length != 0;

      int size = this.entries.length - 1;
      T[] tmpEntries = (Object[])(new Object[size]);
      System.arraycopy(this.entries, 0, tmpEntries, 0, size);
      return new ImmutableStack(tmpEntries);
   }

   public boolean isEmpty() {
      return this.entries.length == 0;
   }

   public int size() {
      return this.entries.length;
   }

   public T get(int i) {
      return this.entries[i];
   }

   public String toString() {
      String objArrayToString = Util.objArrayToString(this.entries);

      assert this.entries.length <= Integer.MAX_VALUE : objArrayToString;

      return objArrayToString;
   }

   public boolean contains(T entry) {
      return Util.arrayContains(this.entries, entry, this.entries.length);
   }

   public boolean topMatches(ImmutableStack<T> other) {
      if (other.size() > this.size()) {
         return false;
      } else {
         int i = other.size() - 1;

         for(int j = this.size() - 1; i >= 0; --j) {
            if (!other.get(i).equals(this.get(j))) {
               return false;
            }

            --i;
         }

         return true;
      }
   }

   public ImmutableStack<T> reverse() {
      T[] tmpEntries = (Object[])(new Object[this.entries.length]);
      int i = this.entries.length - 1;

      for(int j = 0; i >= 0; ++j) {
         tmpEntries[j] = this.entries[i];
         --i;
      }

      return new ImmutableStack(tmpEntries);
   }

   public ImmutableStack<T> popAll(ImmutableStack<T> other) {
      assert this.topMatches(other);

      int size = this.entries.length - other.entries.length;
      T[] tmpEntries = (Object[])(new Object[size]);
      System.arraycopy(this.entries, 0, tmpEntries, 0, size);
      return new ImmutableStack(tmpEntries);
   }

   public ImmutableStack<T> pushAll(ImmutableStack<T> other) {
      int size = this.entries.length + other.entries.length;
      T[] tmpEntries = null;
      if (size <= Integer.MAX_VALUE) {
         tmpEntries = (Object[])(new Object[size]);
         System.arraycopy(this.entries, 0, tmpEntries, 0, this.entries.length);
         System.arraycopy(other.entries, 0, tmpEntries, this.entries.length, other.entries.length);
      } else {
         tmpEntries = (Object[])(new Object[Integer.MAX_VALUE]);
         int numFromThis = Integer.MAX_VALUE - other.entries.length;
         System.arraycopy(this.entries, this.entries.length - numFromThis, tmpEntries, 0, numFromThis);
         System.arraycopy(other.entries, 0, tmpEntries, numFromThis, other.entries.length);
      }

      return new ImmutableStack(tmpEntries);
   }
}
