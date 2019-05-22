package org.testng.internal.collections;

import org.testng.collections.Objects;

public class Pair<A, B> {
   private final A first;
   private final B second;

   public Pair(A first, B second) {
      this.first = first;
      this.second = second;
   }

   public A first() {
      return this.first;
   }

   public B second() {
      return this.second;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.first == null ? 0 : this.first.hashCode());
      result = 31 * result + (this.second == null ? 0 : this.second.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         Pair other = (Pair)obj;
         if (this.first == null) {
            if (other.first != null) {
               return false;
            }
         } else if (!this.first.equals(other.first)) {
            return false;
         }

         if (this.second == null) {
            if (other.second != null) {
               return false;
            }
         } else if (!this.second.equals(other.second)) {
            return false;
         }

         return true;
      }
   }

   public static <A, B> Pair<A, B> create(A first, B second) {
      return of(first, second);
   }

   public static <A, B> Pair<A, B> of(A a, B b) {
      return new Pair(a, b);
   }

   public String toString() {
      return Objects.toStringHelper(this.getClass()).add("first", this.first()).add("second", this.second()).toString();
   }
}
