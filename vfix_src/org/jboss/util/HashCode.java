package org.jboss.util;

import java.io.Serializable;

public final class HashCode implements Serializable, Cloneable, Comparable {
   private static final long serialVersionUID = 2391396931740264021L;
   private static final int NULL_HASHCODE = 0;
   private static final int TRUE_HASHCODE = 1231;
   private static final int FALSE_HASHCODE = 1237;
   private int value;

   public HashCode(int value) {
      this.value = value;
   }

   public HashCode() {
      this(0);
   }

   public HashCode add(boolean b) {
      this.value ^= generate(b);
      return this;
   }

   public HashCode add(byte n) {
      this.value ^= n;
      return this;
   }

   public HashCode add(char n) {
      this.value ^= n;
      return this;
   }

   public HashCode add(short n) {
      this.value ^= n;
      return this;
   }

   public HashCode add(int n) {
      this.value ^= n;
      return this;
   }

   public HashCode add(long n) {
      this.value ^= generate(n);
      return this;
   }

   public HashCode add(float f) {
      this.value ^= generate(f);
      return this;
   }

   public HashCode add(double f) {
      this.value ^= generate(f);
      return this;
   }

   public HashCode add(Object obj) {
      this.value ^= generate(obj);
      return this;
   }

   public int hashCode() {
      return this.value;
   }

   public int compareTo(int other) {
      return this.value < other ? -1 : (this.value == other ? 0 : 1);
   }

   public int compareTo(Object obj) throws ClassCastException {
      HashCode hashCode = (HashCode)obj;
      return this.compareTo(hashCode.value);
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (obj != null && obj.getClass() == this.getClass()) {
         return this.value == ((HashCode)obj).value;
      } else {
         return false;
      }
   }

   public String toString() {
      return String.valueOf(this.value);
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }

   public static int generate(boolean value) {
      return value ? 1231 : 1237;
   }

   public static int generate(long value) {
      return (int)(value ^ value >> 32);
   }

   public static int generate(double value) {
      return generate(Double.doubleToLongBits(value));
   }

   public static int generate(float value) {
      return Float.floatToIntBits(value);
   }

   public static int generate(byte[] bytes) {
      int hashcode = 0;

      for(int i = 0; i < bytes.length; ++i) {
         hashcode <<= 1;
         hashcode ^= bytes[i];
      }

      return hashcode;
   }

   public static int generate(Object[] array, boolean deep) {
      int hashcode = 0;

      for(int i = 0; i < array.length; ++i) {
         if (deep && array[i] instanceof Object[]) {
            hashcode ^= generate((Object[])((Object[])array[i]), true);
         } else {
            hashcode ^= array[i].hashCode();
         }
      }

      return hashcode;
   }

   public static int generate(Object[] array) {
      return generate(array, false);
   }

   public static int generate(Object obj) {
      return obj != null ? obj.hashCode() : 0;
   }
}
