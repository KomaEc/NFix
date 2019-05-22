package com.github.javaparser.ast;

public abstract class DataKey<T> {
   public int hashCode() {
      return this.getClass().hashCode();
   }

   public boolean equals(Object obj) {
      return obj != null && this.getClass().equals(obj.getClass());
   }
}
