package com.gzoltar.shaded.org.pitest.mutationtest.engine;

public class MethodName {
   private final String name;

   MethodName(String name) {
      this.name = name;
   }

   public static MethodName fromString(String name) {
      return new MethodName(name);
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
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
         MethodName other = (MethodName)obj;
         if (this.name == null) {
            if (other.name != null) {
               return false;
            }
         } else if (!this.name.equals(other.name)) {
            return false;
         }

         return true;
      }
   }

   public String toString() {
      return this.name;
   }

   public final String name() {
      return this.name;
   }
}
