package com.gzoltar.shaded.org.pitest.reloc.xstream.core.util;

public final class FastField {
   private final String name;
   private final String declaringClass;

   public FastField(String definedIn, String name) {
      this.name = name;
      this.declaringClass = definedIn;
   }

   public FastField(Class definedIn, String name) {
      this(definedIn == null ? null : definedIn.getName(), name);
   }

   public String getName() {
      return this.name;
   }

   public String getDeclaringClass() {
      return this.declaringClass;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (!(obj instanceof FastField)) {
         return false;
      } else {
         FastField field = (FastField)obj;
         if ((this.declaringClass != null || field.declaringClass == null) && (this.declaringClass == null || field.declaringClass != null)) {
            return this.name.equals(field.getName()) && (this.declaringClass == null || this.declaringClass.equals(field.getDeclaringClass()));
         } else {
            return false;
         }
      }
   }

   public int hashCode() {
      return this.name.hashCode() ^ (this.declaringClass == null ? 0 : this.declaringClass.hashCode());
   }

   public String toString() {
      return (this.declaringClass == null ? "" : this.declaringClass + ".") + this.name;
   }
}
