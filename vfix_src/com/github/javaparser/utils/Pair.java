package com.github.javaparser.utils;

public class Pair<A, B> {
   public final A a;
   public final B b;

   public Pair(A a, B b) {
      this.a = a;
      this.b = b;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Pair<?, ?> pair = (Pair)o;
         if (this.a != null) {
            if (!this.a.equals(pair.a)) {
               return false;
            }
         } else if (pair.a != null) {
            return false;
         }

         if (this.b != null) {
            if (this.b.equals(pair.b)) {
               return true;
            }
         } else if (pair.b == null) {
            return true;
         }

         return false;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.a != null ? this.a.hashCode() : 0;
      return 31 * result + (this.b != null ? this.b.hashCode() : 0);
   }

   public String toString() {
      return CodeGenerationUtils.f("<%s, %s>", this.a, this.b);
   }
}
