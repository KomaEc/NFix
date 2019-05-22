package com.gzoltar.shaded.org.pitest.mutationtest.engine;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;

public final class Location implements Comparable<Location> {
   private final ClassName clazz;
   private final MethodName method;
   private final String methodDesc;

   public Location(ClassName clazz, MethodName method, String methodDesc) {
      this.clazz = clazz;
      this.method = method;
      this.methodDesc = methodDesc;
   }

   public static Location location(ClassName clazz, MethodName method, String methodDesc) {
      return new Location(clazz, method, methodDesc);
   }

   public ClassName getClassName() {
      return this.clazz;
   }

   public MethodName getMethodName() {
      return this.method;
   }

   public String getMethodDesc() {
      return this.methodDesc;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.clazz == null ? 0 : this.clazz.hashCode());
      result = 31 * result + (this.method == null ? 0 : this.method.hashCode());
      result = 31 * result + (this.methodDesc == null ? 0 : this.methodDesc.hashCode());
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
         Location other = (Location)obj;
         if (this.clazz == null) {
            if (other.clazz != null) {
               return false;
            }
         } else if (!this.clazz.equals(other.clazz)) {
            return false;
         }

         if (this.method == null) {
            if (other.method != null) {
               return false;
            }
         } else if (!this.method.equals(other.method)) {
            return false;
         }

         if (this.methodDesc == null) {
            if (other.methodDesc != null) {
               return false;
            }
         } else if (!this.methodDesc.equals(other.methodDesc)) {
            return false;
         }

         return true;
      }
   }

   public String toString() {
      return "Location [clazz=" + this.clazz + ", method=" + this.method + ", methodDesc=" + this.methodDesc + "]";
   }

   public String describe() {
      return this.method.name();
   }

   public int compareTo(Location o) {
      int comp = this.clazz.compareTo(o.getClassName());
      if (comp != 0) {
         return comp;
      } else {
         comp = this.method.name().compareTo(o.getMethodName().name());
         return comp != 0 ? comp : this.methodDesc.compareTo(o.getMethodDesc());
      }
   }
}
